package serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private final PaymentService paymentService;
	private final PlanRepository planRepository;

	public SubscriptionUpgradeServiceImpl(SubscriptionRepository subscriptionRepository, PaymentService paymentService,
			PlanRepository planRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.paymentService = paymentService;
		this.planRepository = planRepository;
	}

	@Override
	public UpgradeSusbcriptionResponse upgradeSubscription(UpgradeSubscriptionRequest request) {
		try {
			validateRequest(request);
			Subscription subscription = getValidatedSubscriptions(request.getSubscriptionID(), request.getUser_id());
			Plan plan = getValidatedPlan(request.getPlanId());
			UpgradeSusbcriptionResponse paymentResponse = paymentService.processPayment(request);
			if (!"success".equalsIgnoreCase(paymentResponse.getStatus())) {
				throw new PaymentProcessingException(paymentResponse.getError());
			}
			updateSubscription(subscription, plan, paymentResponse.getTransactionId());
			return createSuccessResponse(paymentResponse.getTransactionId());
		} catch (PaymentProcessingException | ResourceNotFoundException | IllegalArgumentException ex) {
			return handleUpgradeFailure(ex);
		}
	}

	private void validateRequest(UpgradeSubscriptionRequest request) {
		List<String> validationErrors = new ArrayList<>();
		UserSubscriptionValidator.validateUserSubscription(request, validationErrors);
		if (!validationErrors.isEmpty()) {
			throw new BadRequestException("Validation failed: " + validationErrors);
		}

	}

	private Plan getValidatedPlan(Long planId) {
		Plan plan = planRepository.findById(planId)
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found for ID " + planId));
		if (plan == null) {
			throw new ResourceNotFoundException("Plan not found for ID " + planId);
		}
		if (plan.getNumberOfDays() <= 0) {
			throw new IllegalArgumentException("Plan duration must be greater than 0 days");
		}
		return plan;
	}

	private Subscription getValidatedSubscriptions(Long subscriptionID, Long userId) {
		if (subscriptionID == null) {
			throw new IllegalArgumentException("Invalid Subscription ID provided.");
		}
		return subscriptionRepository.findByIdAndUserId(subscriptionID, userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Subscription not found for ID: " + subscriptionID + " and User ID: " + userId));
	}

	private void updateSubscription(Subscription subscription, Plan plan, String transactionId) {

		LocalDateTime newExpirationDate = LocalDateTime.now().plusDays(plan.getNumberOfDays());

		subscription.setupdationTs(LocalDateTime.now());
		subscription.setTransactionID(transactionId);
		subscription.setExpirationTs(newExpirationDate);
		subscription.setPlan(plan);
		subscriptionRepository.save(subscription);
	}

	private UpgradeSusbcriptionResponse handleUpgradeFailure(RuntimeException ex) {
		UpgradeSusbcriptionResponse response = new UpgradeSusbcriptionResponse();
		response.setStatus("failure");
		response.setError(ex.getMessage());
		response.setTransactionId(null);
		return response;
	}

	private UpgradeSusbcriptionResponse createSuccessResponse(String transactionId) {
		UpgradeSusbcriptionResponse response = new UpgradeSusbcriptionResponse();
		response.setStatus("success");
		response.setTransactionId(transactionId);
		response.setError(null);
		return response;
	}

}
