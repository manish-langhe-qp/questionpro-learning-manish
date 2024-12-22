package service;

import entity.PaymentRequest;

public interface SubscriptionUpgradeService {

	String upgradeSubscription(Long userId, PaymentRequest paymentRequest);

	void autoInitiateTransactions();
}
