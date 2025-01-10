package serviceImpl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import entity.PaymentRequest;
import entity.PaymentResponse;
import exception.PaymentProcessingException;
import service.PaymentService;
import validator.PaymentRequestValidator;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final RestTemplate restTemplate;
	private final PaymentRequestValidator paymentRequestValidator;

	@Value("${payment.gateway.url}")
	private String paymentGatewayUrl;

	public PaymentServiceImpl(RestTemplate restTemplate, PaymentRequestValidator paymentRequestValidator) {
		this.restTemplate = restTemplate;
		this.paymentRequestValidator = paymentRequestValidator;
	}

	@Override
	public PaymentResponse processPayment(PaymentRequest paymentRequest) {
		Objects.requireNonNull(paymentRequest, "PaymentRequest cannot be null");

		paymentRequestValidator.validate(paymentRequest);

		try {
			ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(paymentGatewayUrl, paymentRequest,
					PaymentResponse.class);

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
