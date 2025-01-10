package serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import entity.PaymentRequest;
import entity.PaymentResponse;
import entity.Subscription;
import exception.PaymentProcessingException;
import exception.SubscriptionNotFoundException;
import repository.SubscriptionRepository;
import service.PaymentService;
import service.SubscriptionUpgradeService;
import validator.PaymentRequestValidator;

@Service
public class SubscriptionUpgradeServiceImpl implements SubscriptionUpgradeService {

	private final SubscriptionRepository subscriptionRepository;
	private final PaymentService paymentService;

	public SubscriptionUpgradeServiceImpl(SubscriptionRepository subscriptionRepository,
			PaymentService paymentService) {
		this.subscriptionRepository = subscriptionRepository;
		this.paymentService = paymentService;
	}

	public String upgradeSubscription(Long userId, PaymentRequest paymentRequest) {
		Subscription subscription = subscriptionRepository.findActiveSubscriptionByUserId(userId)
				.orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for userId" + userId));
		PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

		if (!"success".equalsIgnoreCase(paymentResponse.getStatus())) {
			throw new PaymentProcessingException(paymentResponse.getError());
		}

		updateSubscription(subscription, paymentResponse.getTransactionId());
		return "Subscription upgraded successfully! Transaction ID: " + paymentResponse.getTransactionId();
	}

	private void updateSubscription(Subscription subscription, String transactionId) {
		subscription.setCreationTs(LocalDateTime.now());
		subscription.setExpirationTs(LocalDateTime.now().plusMonths(12));
		subscriptionRepository.save(subscription);
	}

}
