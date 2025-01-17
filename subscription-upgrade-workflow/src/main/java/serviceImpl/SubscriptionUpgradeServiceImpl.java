package serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dto.UpgradeSubscriptionRequest;
import dto.UpgradeSusbcriptionResponse;
import entity.Plan;
import entity.Subscription;
import entity.User;
import exception.BadRequestException;
import exception.PaymentProcessingException;
import exception.ResourceNotFoundException;
import repository.PlanRepository;
import repository.SubscriptionRepository;
import repository.UserRepository;
import service.PaymentService;
import service.SubscriptionUpgradeService;
import validator.UserSubscriptionValidator;

@Service
public class SubscriptionUpgradeServiceImpl implements SubscriptionUpgradeService {

	private final SubscriptionRepository subscriptionRepository;
	private final UserRepository userRepository;
	private final PaymentService paymentService;
	private final PlanRepository planRepository;

	public SubscriptionUpgradeServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository,
			PaymentService paymentService, PlanRepository planRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.userRepository = userRepository;
		this.paymentService = paymentService;
		this.planRepository = planRepository;
	}

	public String upgradeSubscription(Long userId, UpgradeSubscriptionRequest userSubscriptionRequest) {
		validateUser(userId);
		Subscription subscription = validateSubscription(userSubscriptionRequest.getSubscriptionID(), userId);
		UserSubscriptionValidator.validateUserSubscription(userSubscriptionRequest);
		Plan plan = getPlanOrThrowException(userSubscriptionRequest.getPlanId());

		UpgradeSusbcriptionResponse paymentResponse = paymentService.processPayment(userSubscriptionRequest);

		if (!"success".equalsIgnoreCase(paymentResponse.getStatus())) {
			throw new PaymentProcessingException(paymentResponse.getError());
		}

		updateSubscription(subscription, paymentResponse.getTransactionId());
		return "Subscription upgraded successfully! Transaction ID: " + paymentResponse.getTransactionId();
	}

	private Plan getPlanOrThrowException(Long planId) {
		Plan plan = planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Plan not found for ID " + planId));
		if (plan == null) {
            throw new ResourceNotFoundException("Plan not found for ID " + planId);
        }
		return plan;
	}

	private Subscription validateSubscription(Long subscriptionID, Long userId) {
		if (subscriptionID == null) {
			throw new IllegalArgumentException("Invalid Subscription ID provided.");
		}
		return subscriptionRepository.findByIdAndUserId(subscriptionID, userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Subscription not found for ID: " + subscriptionID + " and User ID: " + userId));
	}

	private void validateUser(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException("Invalid User ID provided.");
		}
	}

	private void updateSubscription(Subscription subscription, String transactionId) {
		subscription.setCreationTs(LocalDateTime.now());
		subscription.setTransactionID(transactionId);
		subscription.setExpirationTs(LocalDateTime.now().plusMonths(12));
		subscriptionRepository.save(subscription);
	}

}
