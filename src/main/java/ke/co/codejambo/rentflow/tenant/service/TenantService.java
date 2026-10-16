package ke.co.codejambo.rentflow.tenant.service;

import ke.co.codejambo.rentflow.tenant.dto.CreateTenantRequest;
import ke.co.codejambo.rentflow.tenant.entity.Tenant;
import ke.co.codejambo.rentflow.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@Service
public class TenantService {

    private final TenantRepository repository;

    public TenantService(TenantRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Tenant create(CreateTenantRequest req, UUID actorId) {
        String nidHash = sha256Hex(req.nationalId());
        if (repository.findByNationalIdHashAndDeletedAtIsNull(nidHash).isPresent()) {
            throw new IllegalStateException("Tenant with this national ID already exists");
        }

        Tenant t = new Tenant();
        t.setFullName(req.fullName());
        t.setPhoneHash(sha256Hex(req.phone()));
        if (req.mpesaPhone() != null && !req.mpesaPhone().isBlank()) {
            t.setMpesaPhoneHash(sha256Hex(req.mpesaPhone()));
        }
        t.setEmail(req.email());
        t.setNationalIdHash(nidHash);
        t.setStatus("ACTIVE");
        t.setCreatedBy(actorId);
        t.setUpdatedBy(actorId);

        return repository.save(t);
    }

    @Transactional(readOnly = true)
    public Tenant getById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new IllegalStateException("Tenant not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Tenant> listAll() {
        return repository.findByDeletedAtIsNull();
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Hash failure", e);
        }
    }
}
