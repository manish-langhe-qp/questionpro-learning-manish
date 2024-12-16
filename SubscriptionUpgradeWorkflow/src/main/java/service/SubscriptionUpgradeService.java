package service;

import entity.PaymentRequest;

public interface SubscriptionUpgradeService {

	String upgradeSubscription(Long subscriptionId, PaymentRequest paymentRequest);

	void autoInitiateTransactions();
}
