package ke.co.codejambo.rentflow.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateTenantRequest(
    @NotBlank String fullName,
    @NotBlank @Pattern(regexp = "^\\+254[0-9]{9}$") String phone,
    @Pattern(regexp = "^\\+254[0-9]{9}$") String mpesaPhone,
    String email,
    @NotBlank String nationalId
) {}