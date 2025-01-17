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

import dto.UpgradeSubscriptionRequest;
import dto.UpgradeSusbcriptionResponse;
import jakarta.validation.Valid;
import schedulerService.SubscriptionUpgradeScheduler;
import service.SubscriptionUpgradeService;

@RestController
@RequestMapping("api/v1/subscriptions")
@Validated
public class SubscriptionController {
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
	private final SubscriptionUpgradeService subscriptionUpgradeService;

	public SubscriptionController(SubscriptionUpgradeService subscriptionUpgradeService,
			SubscriptionUpgradeScheduler subscriptionUpgradeScheduler) {
		super();
		this.subscriptionUpgradeService = subscriptionUpgradeService;
	}

	@PostMapping("/{subscriptionId}/upgrade")
	public ResponseEntity<UpgradeSusbcriptionResponse> upgradeSubscription(@Valid @RequestBody UpgradeSubscriptionRequest ueSubscriptionRequest) {
		logger.info("Attempting to upgrade subscription: {}", ueSubscriptionRequest.getSubscriptionID());
		UpgradeSusbcriptionResponse response = subscriptionUpgradeService.upgradeSubscription(ueSubscriptionRequest);
		return ResponseEntity.ok(response);
	}
}
