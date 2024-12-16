package validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import entity.PaymentRequest;

@Component
public class PaymentRequestValidator {

	public void validate(PaymentRequest request) {
		if (!StringUtils.hasText(request.getName()) || !request.getName().matches("^[a-zA-Z ]+$")) {
			throw new IllegalArgumentException("Invalid name on card.");
		}
		if (!StringUtils.hasText(request.getCardNumber()) || !request.getCardNumber().matches("^\\d{16}$")) {
			throw new IllegalArgumentException("Invalid card number.");
		}
		if (!StringUtils.hasText(request.getCvv()) || !request.getCvv().matches("^\\d{3}$")) {
			throw new IllegalArgumentException("Invalid CVV.");
		}
		if (!StringUtils.hasText(request.getExpiryDate())
				|| !request.getExpiryDate().matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
			throw new IllegalArgumentException("Invalid expiry date.");
		}
	}
}
