package ke.co.codejambo.rentflow.tenant.repository;

import ke.co.codejambo.rentflow.tenant.entity.Lease;
import ke.co.codejambo.rentflow.tenant.entity.LeaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaseRepository extends JpaRepository<Lease, UUID> {
    Optional<Lease> findByIdAndDeletedAtIsNull(UUID id);
    List<Lease> findByTenantIdAndDeletedAtIsNull(UUID tenantId);
    List<Lease> findByUnitIdAndStatusAndDeletedAtIsNull(UUID unitId, LeaseStatus status);
    List<Lease> findByDeletedAtIsNull();
}
