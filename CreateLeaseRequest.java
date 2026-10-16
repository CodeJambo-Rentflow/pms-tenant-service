package ke.co.codejambo.rentflow.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record CreateLeaseRequest(
    @NotNull UUID tenantId,
    @NotNull UUID unitId,
    @NotNull UUID buildingId,
    @NotBlank String leaseNumber,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotNull @Positive Long monthlyRent,
    Long depositAmount,
    Short paymentDueDay
) {}