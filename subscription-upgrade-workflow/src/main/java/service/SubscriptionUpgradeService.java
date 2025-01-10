package service;

import entity.PaymentRequest;

public interface SubscriptionUpgradeService {

	public String upgradeSubscription(Long userId, PaymentRequest paymentRequest);

}
