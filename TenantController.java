package ke.co.codejambo.rentflow.tenant.controller;

import jakarta.validation.Valid;
import ke.co.codejambo.rentflow.tenant.dto.CreateTenantRequest;
import ke.co.codejambo.rentflow.tenant.dto.TenantResponse;
import ke.co.codejambo.rentflow.tenant.service.TenantService;
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
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantService service;

    public TenantController(TenantService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<TenantResponse> create(
            @Valid @RequestBody CreateTenantRequest req,
            @AuthenticationPrincipal Jwt jwt) {
        UUID actorId = null;
        try { actorId = UUID.fromString(jwt.getSubject()); } catch (Exception ignored) {}
        return ResponseEntity.status(HttpStatus.CREATED).body(TenantResponse.from(service.create(req, actorId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') or hasRole('SYSTEM_ADMIN')")
    public List<TenantResponse> list() {
        return service.listAll().stream().map(TenantResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') or hasRole('SYSTEM_ADMIN')")
    public TenantResponse getById(@PathVariable UUID id) {
        return TenantResponse.from(service.getById(id));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "success", false,
            "error", Map.of("code", "CONFLICT", "message", ex.getMessage())
        ));
    }
}