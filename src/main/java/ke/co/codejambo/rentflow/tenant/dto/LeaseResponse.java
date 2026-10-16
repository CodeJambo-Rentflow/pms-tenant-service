package ke.co.codejambo.rentflow.tenant.dto;

import ke.co.codejambo.rentflow.tenant.entity.Lease;
import ke.co.codejambo.rentflow.tenant.entity.LeaseStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LeaseResponse(
    UUID id,
    String leaseNumber,
    UUID tenantId,
    UUID unitId,
    UUID buildingId,
    LocalDate startDate,
    LocalDate endDate,
    Long monthlyRent,
    String currency,
    Long depositAmount,
    Short paymentDueDay,
    LeaseStatus status,
    OffsetDateTime createdAt
) {
    public static LeaseResponse from(Lease l) {
        return new LeaseResponse(
            l.getId(), l.getLeaseNumber(), l.getTenantId(), l.getUnitId(), l.getBuildingId(),
            l.getStartDate(), l.getEndDate(), l.getMonthlyRent(), l.getCurrency(),
            l.getDepositAmount(), l.getPaymentDueDay(), l.getStatus(), l.getCreatedAt()
        );
    }
}
