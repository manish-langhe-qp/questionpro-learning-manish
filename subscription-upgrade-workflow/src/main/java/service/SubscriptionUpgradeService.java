package service;

import dto.UpgradeSubscriptionRequest;

public interface SubscriptionUpgradeService {

	public String upgradeSubscription(Long userId, UpgradeSubscriptionRequest paymentRequest);

}
