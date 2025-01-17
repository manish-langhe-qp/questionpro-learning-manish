package service;

import dto.UpgradeSubscriptionRequest;
import dto.UpgradeSusbcriptionResponse;

public interface SubscriptionUpgradeService {

	public UpgradeSusbcriptionResponse upgradeSubscription(UpgradeSubscriptionRequest userSubscriptionRequest);

}
