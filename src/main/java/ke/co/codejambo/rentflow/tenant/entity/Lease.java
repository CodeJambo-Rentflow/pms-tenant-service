package ke.co.codejambo.rentflow.tenant.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "leases", schema = "tenant")
public class Lease {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "unit_id", nullable = false)
    private UUID unitId;

    @Column(name = "building_id", nullable = false)
    private UUID buildingId;

    @Column(name = "lease_number", nullable = false, unique = true, length = 50)
    private String leaseNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "monthly_rent", nullable = false)
    private Long monthlyRent;

    @Column(nullable = false, length = 3)
    private String currency = "KES";

    @Column(name = "deposit_amount", nullable = false)
    private Long depositAmount = 0L;

    @Column(name = "payment_due_day", nullable = false)
    private Short paymentDueDay = 5;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency", nullable = false, length = 20)
    private PaymentFrequency paymentFrequency = PaymentFrequency.MONTHLY;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "late_fee_policy", columnDefinition = "jsonb")
    private String lateFeePolicy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LeaseStatus status = LeaseStatus.DRAFT;

    @Column(name = "signed_at")
    private OffsetDateTime signedAt;

    @Column(name = "move_in_date")
    private LocalDate moveInDate;

    @Column(name = "move_out_date")
    private LocalDate moveOutDate;

    @Column(name = "move_out_notes", columnDefinition = "TEXT")
    private String moveOutNotes;

    @Column(name = "terminated_reason", columnDefinition = "TEXT")
    private String terminatedReason;

    @Column(name = "previous_lease_id")
    private UUID previousLeaseId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = OffsetDateTime.now(); }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getUnitId() { return unitId; }
    public void setUnitId(UUID unitId) { this.unitId = unitId; }
    public UUID getBuildingId() { return buildingId; }
    public void setBuildingId(UUID buildingId) { this.buildingId = buildingId; }
    public String getLeaseNumber() { return leaseNumber; }
    public void setLeaseNumber(String leaseNumber) { this.leaseNumber = leaseNumber; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Long getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Long monthlyRent) { this.monthlyRent = monthlyRent; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Long getDepositAmount() { return depositAmount; }
    public void setDepositAmount(Long depositAmount) { this.depositAmount = depositAmount; }
    public Short getPaymentDueDay() { return paymentDueDay; }
    public void setPaymentDueDay(Short paymentDueDay) { this.paymentDueDay = paymentDueDay; }
    public PaymentFrequency getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(PaymentFrequency paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getLateFeePolicy() { return lateFeePolicy; }
    public void setLateFeePolicy(String lateFeePolicy) { this.lateFeePolicy = lateFeePolicy; }
    public LeaseStatus getStatus() { return status; }
    public void setStatus(LeaseStatus status) { this.status = status; }
    public OffsetDateTime getSignedAt() { return signedAt; }
    public void setSignedAt(OffsetDateTime signedAt) { this.signedAt = signedAt; }
    public LocalDate getMoveInDate() { return moveInDate; }
    public void setMoveInDate(LocalDate moveInDate) { this.moveInDate = moveInDate; }
    public LocalDate getMoveOutDate() { return moveOutDate; }
    public void setMoveOutDate(LocalDate moveOutDate) { this.moveOutDate = moveOutDate; }
    public String getMoveOutNotes() { return moveOutNotes; }
    public void setMoveOutNotes(String moveOutNotes) { this.moveOutNotes = moveOutNotes; }
    public String getTerminatedReason() { return terminatedReason; }
    public void setTerminatedReason(String terminatedReason) { this.terminatedReason = terminatedReason; }
    public UUID getPreviousLeaseId() { return previousLeaseId; }
    public void setPreviousLeaseId(UUID previousLeaseId) { this.previousLeaseId = previousLeaseId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
}
