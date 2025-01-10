package serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import entity.Subscription;
import jakarta.transaction.Transactional;
import repository.SubscriptionRepository;

public class SubscriptionUpgradeScheduler {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionUpgradeScheduler.class);

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionUpgradeScheduler(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	@Scheduled(cron = "0 0 12 * * ?") // Runs daily at 12 PM
	public void handlePendingSubscriptions() {
		LOGGER.info("Starting subscription check for pending or expiring subscriptions...");

		List<Subscription> pendingSubscriptions = subscriptionRepository
				.findPendingOrExpiringSubscriptions(LocalDateTime.now());
		if (pendingSubscriptions.isEmpty()) {
			LOGGER.info("No pending or expiring subscriptions found.");
			return;
		}

		pendingSubscriptions.forEach(subscription -> {
			try {
				handleSubscription(subscription);
			} catch (Exception e) {
				LOGGER.error("Error handling subscription ID: {} - Error: {}", subscription.getSubscriptionId(),
						e.getMessage());
			}
		});

		LOGGER.info("Completed subscription processing for pending or expiring subscriptions.");
	}
}
