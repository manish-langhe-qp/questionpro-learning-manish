package serviceImpl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dto.UpgradeSubscriptionRequest;
import dto.UpgradeSusbcriptionResponse;
import exception.PaymentProcessingException;
import service.PaymentService;
import validator.UserSubscriptionValidator;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final RestTemplate restTemplate;
	private final UserSubscriptionValidator paymentRequestValidator;

	@Value("${payment.gateway.url}")
	private String paymentGatewayUrl;

	public PaymentServiceImpl(RestTemplate restTemplate, UserSubscriptionValidator paymentRequestValidator) {
		this.restTemplate = restTemplate;
		this.paymentRequestValidator = paymentRequestValidator;
	}

	@Override
	public UpgradeSusbcriptionResponse processPayment(UpgradeSubscriptionRequest paymentRequest) {
		Objects.requireNonNull(paymentRequest, "PaymentRequest cannot be null");

		try {
			ResponseEntity<UpgradeSusbcriptionResponse> response = restTemplate.postForEntity(paymentGatewayUrl, paymentRequest,
					UpgradeSusbcriptionResponse.class);

			if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
				throw new PaymentProcessingException(
						"Failed to process payment. Response code: " + response.getStatusCode());
			}

			return response.getBody();

		} catch (Exception ex) {
			throw new PaymentProcessingException("An error occurred while processing the payment: " + ex.getMessage());
		}
	}

}
