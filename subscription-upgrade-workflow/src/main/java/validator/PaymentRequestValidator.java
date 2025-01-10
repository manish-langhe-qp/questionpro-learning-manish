package validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import entity.PaymentRequest;

@Component
public class PaymentRequestValidator {

	private static final String NAME_REGEX = "^[a-zA-Z ]+$";
	private static final String CARD_NUMBER_REGEX = "^\\d{16}$";
	private static final String CVV_REGEX = "^\\d{3}$";
	private static final String EXPIRY_DATE_REGEX = "^(0[1-9]|1[0-2])/\\d{2}$";

	private static final String INVALID_NAME_ERROR = "Invalid name on card.";
	private static final String INVALID_CARD_NUMBER_ERROR = "Invalid card number.";
	private static final String INVALID_CVV_ERROR = "Invalid CVV.";
	private static final String INVALID_EXPIRY_DATE_ERROR = "Invalid expiry date.";

	public void validate(PaymentRequest request) {
		if (!StringUtils.hasText(request.getName()) || !request.getName().matches(NAME_REGEX)) {
			throw new IllegalArgumentException(INVALID_NAME_ERROR);
		}
		if (!StringUtils.hasText(request.getCardNumber()) || !request.getCardNumber().matches(CARD_NUMBER_REGEX)) {
			throw new IllegalArgumentException(INVALID_CARD_NUMBER_ERROR);
		}
		if (!StringUtils.hasText(request.getCvv()) || !request.getCvv().matches(CVV_REGEX)) {
			throw new IllegalArgumentException(INVALID_CVV_ERROR);
		}
		if (!StringUtils.hasText(request.getExpiryDate()) || !request.getExpiryDate().matches(EXPIRY_DATE_REGEX)) {
			throw new IllegalArgumentException(INVALID_EXPIRY_DATE_ERROR);
		}
	}
}
