package ke.co.codejambo.rentflow.tenant.repository;

import ke.co.codejambo.rentflow.tenant.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
    Optional<Deposit> findByLeaseId(UUID leaseId);
    List<Deposit> findByTenantId(UUID tenantId);
}
