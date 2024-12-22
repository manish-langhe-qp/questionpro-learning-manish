package service;

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
import repository.SubscriptionRepository;
import validator.PaymentRequestValidator;

@Service
public class SubscriptionUpgradeServiceImpl implements SubscriptionUpgradeService {

	private final RestTemplate restTemplate;
	private final SubscriptionRepository subscriptionRepository;
	private final PaymentRequestValidator paymentRequestValidator;

	@Value("${payment.gateway.url}")
	private String paymentGatewayUrl;

	public SubscriptionUpgradeServiceImpl(RestTemplate restTemplate, SubscriptionRepository subscriptionRepository,
			PaymentRequestValidator paymentRequestValidator) {
		this.restTemplate = restTemplate;
		this.subscriptionRepository = subscriptionRepository;
		this.paymentRequestValidator = paymentRequestValidator;
	}

	@Override
	public String upgradeSubscription(Long userId, PaymentRequest paymentRequest) {
		paymentRequestValidator.validate(paymentRequest);
		Subscription subscription = getActiveSubscriptionByUserId(userId);
		PaymentResponse paymentResponse = processPayment(paymentRequest);

		if (!"success".equalsIgnoreCase(paymentResponse.getStatus())) {
			throw new PaymentProcessingException(paymentResponse.getError());
		}

		updateSubscription(subscription, paymentResponse.getTransactionId());
		return "Subscription upgraded successfully! Transaction ID: " + paymentResponse.getTransactionId();
	}

	private Subscription getActiveSubscriptionByUserId(Long userId) {
		return subscriptionRepository.findActiveSubscriptionByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException("No active subscription found for user ID: " + userId));
	}

	private PaymentResponse processPayment(PaymentRequest paymentRequest) {
		ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(paymentGatewayUrl, paymentRequest,
				PaymentResponse.class);
		if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
			throw new PaymentProcessingException("Failed to process payment.");
		}
		return response.getBody();
	}

	private void updateSubscription(Subscription subscription, String transactionId) {
		subscription.setCreationTs(LocalDateTime.now());
		subscription.setExpirationTs(LocalDateTime.now().plusMonths(12));
		subscriptionRepository.save(subscription);
	}

	@Override
	@Scheduled(cron = "0 0 12 * * ?") // Runs daily at 12 PM
	public void autoInitiateTransactions() {
		List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsToUpgrade();
		subscriptions.forEach(subscription -> {
			PaymentRequest paymentRequest = preparePaymentRequest(subscription);
			try {
				upgradeSubscription(subscription.getUser().getId(), paymentRequest);
			} catch (Exception e) {
				System.err.println("Failed to process subscription ID: " + subscription.getSubscriptionId()
						+ " - Error: " + e.getMessage());
			}
		});

	}

	private PaymentRequest preparePaymentRequest(Subscription subscription) {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setName("Test User");
		paymentRequest.setCardNumber("4111111111111111");
		paymentRequest.setCvv("123");
		paymentRequest.setExpiryDate("12/25");
		return paymentRequest;
	}

}
