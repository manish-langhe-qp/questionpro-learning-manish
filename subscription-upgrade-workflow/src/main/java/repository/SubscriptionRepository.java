package repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Optional<Subscription> findActiveSubscriptionByUserId(Long userId);

	@Query("SELECT s FROM Subscription s WHERE s.expirationTs <= :CURRENT_TIMESTAMP OR s.status = 'PENDING_PAYMENT'")
	List<Subscription> findPendingOrExpiringSubscriptions(@Param("CURRENT_TIMESTAMP") LocalDateTime CURRENT_TIMESTAMP);
}
