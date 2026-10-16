package ke.co.codejambo.rentflow.tenant.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TerminateLeaseRequest(
    @NotNull LocalDate moveOutDate,
    String reason,
    String notes
) {}