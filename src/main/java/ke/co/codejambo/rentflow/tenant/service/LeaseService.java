package ke.co.codejambo.rentflow.tenant.service;

import ke.co.codejambo.rentflow.tenant.dto.CreateLeaseRequest;
import ke.co.codejambo.rentflow.tenant.dto.TerminateLeaseRequest;
import ke.co.codejambo.rentflow.tenant.entity.Deposit;
import ke.co.codejambo.rentflow.tenant.entity.DepositStatus;
import ke.co.codejambo.rentflow.tenant.entity.Lease;
import ke.co.codejambo.rentflow.tenant.entity.LeaseStatus;
import ke.co.codejambo.rentflow.tenant.repository.DepositRepository;
import ke.co.codejambo.rentflow.tenant.repository.LeaseRepository;
import ke.co.codejambo.rentflow.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LeaseService {

    private final LeaseRepository leaseRepository;
    private final TenantRepository tenantRepository;
    private final DepositRepository depositRepository;

    public LeaseService(LeaseRepository leaseRepository,
                        TenantRepository tenantRepository,
                        DepositRepository depositRepository) {
        this.leaseRepository = leaseRepository;
        this.tenantRepository = tenantRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public Lease create(CreateLeaseRequest req, UUID actorId) {
        tenantRepository.findByIdAndDeletedAtIsNull(req.tenantId())
            .orElseThrow(() -> new IllegalStateException("Tenant not found: " + req.tenantId()));

        List<Lease> existing = leaseRepository.findByUnitIdAndStatusAndDeletedAtIsNull(req.unitId(), LeaseStatus.ACTIVE);
        if (!existing.isEmpty()) {
            throw new IllegalStateException("Unit already has an active lease");
        }

        Lease l = new Lease();
        l.setTenantId(req.tenantId());
        l.setUnitId(req.unitId());
        l.setBuildingId(req.buildingId());
        l.setLeaseNumber(req.leaseNumber());
        l.setStartDate(req.startDate());
        l.setEndDate(req.endDate());
        l.setMonthlyRent(req.monthlyRent());
        l.setDepositAmount(req.depositAmount() != null ? req.depositAmount() : 0L);
        if (req.paymentDueDay() != null) l.setPaymentDueDay(req.paymentDueDay());
        l.setStatus(LeaseStatus.ACTIVE);
        l.setSignedAt(OffsetDateTime.now());
        l.setCreatedBy(actorId);
        l.setUpdatedBy(actorId);

        Lease saved = leaseRepository.save(l);

        if (saved.getDepositAmount() > 0) {
            Deposit d = new Deposit();
            d.setLeaseId(saved.getId());
            d.setTenantId(saved.getTenantId());
            d.setAmount(saved.getDepositAmount());
            d.setHeldSince(OffsetDateTime.now());
            depositRepository.save(d);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public Lease getById(UUID id) {
        return leaseRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new IllegalStateException("Lease not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Lease> listByTenant(UUID tenantId) {
        return leaseRepository.findByTenantIdAndDeletedAtIsNull(tenantId);
    }

    @Transactional(readOnly = true)
    public List<Lease> listAll() {
        return leaseRepository.findByDeletedAtIsNull();
    }

    @Transactional
    public Lease terminate(UUID leaseId, TerminateLeaseRequest req, UUID actorId) {
        Lease l = getById(leaseId);
        if (l.getStatus() != LeaseStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE leases can be terminated");
        }
        l.setStatus(LeaseStatus.TERMINATED);
        l.setMoveOutDate(req.moveOutDate());
        l.setMoveOutNotes(req.notes());
        l.setTerminatedReason(req.reason());
        l.setUpdatedBy(actorId);

        depositRepository.findByLeaseId(leaseId).ifPresent(d -> {
            d.setStatus(DepositStatus.REFUND_PENDING);
            d.setRefundAmount(d.getAmount());
            d.setRefundDueDate(req.moveOutDate().plusDays(21));
            depositRepository.save(d);
        });

        return leaseRepository.save(l);
    }
}
