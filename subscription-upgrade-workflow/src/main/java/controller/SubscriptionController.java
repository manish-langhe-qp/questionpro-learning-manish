package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.PaymentRequest;
import jakarta.validation.Valid;
import service.SubscriptionUpgradeService;

@RestController
@RequestMapping("api/v1/subscriptions")
@Validated
public class SubscriptionController {
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
	private final SubscriptionUpgradeService subscriptionUpgradeService;

	public SubscriptionController(SubscriptionUpgradeService subscriptionUpgradeService) {
		this.subscriptionUpgradeService = subscriptionUpgradeService;
	}

	@PostMapping("/{subscriptionId}/upgrade")
	public ResponseEntity<String> upgradeSubscription(@PathVariable Long subscriptionId,
			@Valid @RequestBody PaymentRequest paymentRequest) {
		logger.info("Attempting to upgrade subscription: {}", subscriptionId);
		String message = subscriptionUpgradeService.upgradeSubscription(subscriptionId, paymentRequest);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/batch-upgrade")
	public ResponseEntity<String> initiateBatchUpgrade() {
		logger.info("Starting batch upgrade process.");
		subscriptionUpgradeService.autoInitiateTransactions();
		return ResponseEntity.ok("Batch upgrade process started successfully.");
	}
}
