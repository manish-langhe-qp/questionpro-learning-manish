package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Optional<Subscription> findActiveSubscriptionByUserId(Long userId);

	@Query("SELECT s FROM Subscription s WHERE s.expirationTs < CURRENT_TIMESTAMP")
	List<Subscription> findSubscriptionsToUpgrade();
}
