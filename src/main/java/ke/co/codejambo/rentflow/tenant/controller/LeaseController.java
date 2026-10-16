package ke.co.codejambo.rentflow.tenant.controller;

import jakarta.validation.Valid;
import ke.co.codejambo.rentflow.tenant.dto.CreateLeaseRequest;
import ke.co.codejambo.rentflow.tenant.dto.LeaseResponse;
import ke.co.codejambo.rentflow.tenant.dto.TerminateLeaseRequest;
import ke.co.codejambo.rentflow.tenant.service.LeaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leases")
public class LeaseController {

    private final LeaseService service;

    public LeaseController(LeaseService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<LeaseResponse> create(
            @Valid @RequestBody CreateLeaseRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        UUID actorId = null;
        try { actorId = UUID.fromString(jwt.getSubject()); } catch (Exception ignored) {}
        return ResponseEntity.status(HttpStatus.CREATED).body(LeaseResponse.from(service.create(req, actorId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') or hasRole('SYSTEM_ADMIN')")
    public List<LeaseResponse> list() {
        return service.listAll().stream().map(LeaseResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') or hasRole('SYSTEM_ADMIN')")
    public LeaseResponse getById(@PathVariable UUID id) {
        return LeaseResponse.from(service.getById(id));
    }

    @PostMapping("/{id}/terminate")
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') or hasRole('SYSTEM_ADMIN')")
    public LeaseResponse terminate(
            @PathVariable UUID id,
            @Valid @RequestBody TerminateLeaseRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        UUID actorId = null;
        try { actorId = UUID.fromString(jwt.getSubject()); } catch (Exception ignored) {}
        return LeaseResponse.from(service.terminate(id, req, actorId));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "success", false,
            "error", Map.of("code", "CONFLICT", "message", ex.getMessage())
        ));
    }
}
