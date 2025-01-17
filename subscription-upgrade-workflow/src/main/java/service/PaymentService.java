package service;

import dto.UpgradeSubscriptionRequest;
import dto.UpgradeSusbcriptionResponse;

public interface PaymentService {

	public UpgradeSusbcriptionResponse processPayment(UpgradeSubscriptionRequest paymentRequest);
}
