package ke.co.codejambo.rentflow.tenant.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "deposits", schema = "tenant")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lease_id", nullable = false)
    private UUID leaseId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, length = 3)
    private String currency = "KES";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DepositStatus status = DepositStatus.HELD;

    @Column(name = "held_since", nullable = false)
    private OffsetDateTime heldSince;

    @Column(name = "refund_amount")
    private Long refundAmount;

    @Column(name = "refund_due_date")
    private LocalDate refundDueDate;

    @Column(name = "refund_paid_at")
    private OffsetDateTime refundPaidAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String deductions;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.heldSince == null) this.heldSince = now;
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = OffsetDateTime.now(); }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getLeaseId() { return leaseId; }
    public void setLeaseId(UUID leaseId) { this.leaseId = leaseId; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public DepositStatus getStatus() { return status; }
    public void setStatus(DepositStatus status) { this.status = status; }
    public OffsetDateTime getHeldSince() { return heldSince; }
    public void setHeldSince(OffsetDateTime heldSince) { this.heldSince = heldSince; }
    public Long getRefundAmount() { return refundAmount; }
    public void setRefundAmount(Long refundAmount) { this.refundAmount = refundAmount; }
    public LocalDate getRefundDueDate() { return refundDueDate; }
    public void setRefundDueDate(LocalDate refundDueDate) { this.refundDueDate = refundDueDate; }
    public OffsetDateTime getRefundPaidAt() { return refundPaidAt; }
    public void setRefundPaidAt(OffsetDateTime refundPaidAt) { this.refundPaidAt = refundPaidAt; }
    public String getDeductions() { return deductions; }
    public void setDeductions(String deductions) { this.deductions = deductions; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
