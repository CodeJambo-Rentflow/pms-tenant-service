package ke.co.codejambo.rentflow.tenant.repository;

import ke.co.codejambo.rentflow.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByIdAndDeletedAtIsNull(UUID id);
    Optional<Tenant> findByNationalIdHashAndDeletedAtIsNull(String nationalIdHash);
    List<Tenant> findByDeletedAtIsNull();
}
