package ke.co.codejambo.rentflow.tenant.dto;

import ke.co.codejambo.rentflow.tenant.entity.Tenant;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TenantResponse(
    UUID id,
    String fullName,
    String email,
    String status,
    OffsetDateTime createdAt
) {
    public static TenantResponse from(Tenant t) {
        return new TenantResponse(t.getId(), t.getFullName(), t.getEmail(), t.getStatus(), t.getCreatedAt());
    }
}