package ke.co.codejambo.rentflow.tenant.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_profile_id")
    private UUID userProfileId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_encrypted", columnDefinition = "bytea")
    private byte[] phoneEncrypted;

    @Column(name = "phone_hash", length = 64)
    private String phoneHash;

    @Column(name = "mpesa_phone_encrypted", columnDefinition = "bytea")
    private byte[] mpesaPhoneEncrypted;

    @Column(name = "mpesa_phone_hash", length = 64)
    private String mpesaPhoneHash;

    private String email;

    @Column(name = "national_id_encrypted", columnDefinition = "bytea")
    private byte[] nationalIdEncrypted;

    @Column(name = "national_id_hash", length = 64, nullable = false)
    private String nationalIdHash;

    @Column(name = "date_of_birth")
    private java.time.LocalDate dateOfBirth;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone_encrypted", columnDefinition = "bytea")
    private byte[] emergencyContactPhoneEncrypted;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

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
    public UUID getUserProfileId() { return userProfileId; }
    public void setUserProfileId(UUID userProfileId) { this.userProfileId = userProfileId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public byte[] getPhoneEncrypted() { return phoneEncrypted; }
    public void setPhoneEncrypted(byte[] phoneEncrypted) { this.phoneEncrypted = phoneEncrypted; }
    public String getPhoneHash() { return phoneHash; }
    public void setPhoneHash(String phoneHash) { this.phoneHash = phoneHash; }
    public byte[] getMpesaPhoneEncrypted() { return mpesaPhoneEncrypted; }
    public void setMpesaPhoneEncrypted(byte[] mpesaPhoneEncrypted) { this.mpesaPhoneEncrypted = mpesaPhoneEncrypted; }
    public String getMpesaPhoneHash() { return mpesaPhoneHash; }
    public void setMpesaPhoneHash(String mpesaPhoneHash) { this.mpesaPhoneHash = mpesaPhoneHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public byte[] getNationalIdEncrypted() { return nationalIdEncrypted; }
    public void setNationalIdEncrypted(byte[] nationalIdEncrypted) { this.nationalIdEncrypted = nationalIdEncrypted; }
    public String getNationalIdHash() { return nationalIdHash; }
    public void setNationalIdHash(String nationalIdHash) { this.nationalIdHash = nationalIdHash; }
    public java.time.LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(java.time.LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public byte[] getEmergencyContactPhoneEncrypted() { return emergencyContactPhoneEncrypted; }
    public void setEmergencyContactPhoneEncrypted(byte[] emergencyContactPhoneEncrypted) { this.emergencyContactPhoneEncrypted = emergencyContactPhoneEncrypted; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public UUID getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
}
