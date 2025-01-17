package repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	@Query("SELECT s FROM subscription s WHERE s.subscriptionId = subscriptionId AND s.userId = userId")
	Optional<Subscription> findByIdAndUserId(Long subscriptionId, Long userId);

	@Query("SELECT s FROM subscription s WHERE s.expirationTs <= :CURRENT_TIMESTAMP OR s.status = 'PENDING_PAYMENT'")
	List<Subscription> findPendingOrExpiringSubscriptions(@Param("CURRENT_TIMESTAMP") LocalDateTime CURRENT_TIMESTAMP);
}
