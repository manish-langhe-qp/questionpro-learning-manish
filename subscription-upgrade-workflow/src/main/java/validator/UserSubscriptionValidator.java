package validator;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import dto.UpgradeSubscriptionRequest;
import exception.BadRequestException;
import exception.PaymentProcessingException;

@Component
public class UserSubscriptionValidator {

	private static final String ERROR_CARD_NUMBER = "Card number is invalid. It must be exactly 16 digits and contain only numbers.";
	private static final String ERROR_CARD_CVV = "Card CVV is invalid. It must be exactly 3 digits and contain only numbers.";
	private static final String ERROR_CARD_EXPIRY_DATE_FORMAT = "Card expiry date format is invalid. It must be in MM/YY format.";
	private static final String ERROR_CARD_EXPIRED = "Card expiry date is invalid. The card is already expired.";
	private static final String ERROR_CARD_OWNER_NAME = "Card owner name is invalid. It must include at least a first and last name.";
	private static final String ERROR_USER_ID = "User ID must be a valid positive number.";
	private static final String ERROR_SUBSCRIPTION_ID = "Subscription ID must be a valid positive number.";

	public static void validateUserSubscription(UpgradeSubscriptionRequest request, List<String> validationErrors) {
		validateCardNumber(request.getCardNumber(), validationErrors);
		validateCardCvv(request.getCvv(), validationErrors);
		validateExpiryDate(request.getExpiryDate(), validationErrors);
		validateCardOwnerName(request.getName(), validationErrors);
		validateUserId(request.getUser_id(), validationErrors);
		validateSubscriptionId(request.getSubscriptionID(), validationErrors);
	}

	private static void validateCardNumber(String cardNumber, List<String> errorList) {
		if (cardNumber == null || cardNumber.length() != 16 || !StringUtils.hasText(cardNumber)) {
			errorList.add(ERROR_CARD_NUMBER);
		}
	}

	private static void validateCardCvv(String cardCvv, List<String> errorList) {
		if (cardCvv == null || cardCvv.length() != 3 || !StringUtils.hasText(cardCvv)) {
			errorList.add(ERROR_CARD_CVV);
		}
	}

	private static void validateExpiryDate(String expiryDate, List<String> errorList) {
		if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
			errorList.add(ERROR_CARD_EXPIRY_DATE_FORMAT);
			return;
		}

		String[] parts = expiryDate.split("/");
		int month = Integer.parseInt(parts[0]);
		int year = 2000 + Integer.parseInt(parts[1]);

		YearMonth currentYearMonth = YearMonth.now();
		YearMonth cardYearMonth = YearMonth.of(year, month);

		if (cardYearMonth.isBefore(currentYearMonth)) {
			errorList.add(ERROR_CARD_EXPIRED);
		}
	}

	private static void validateCardOwnerName(String cardOwnerName, List<String> errorList) {
		if (cardOwnerName == null || !StringUtils.hasText(cardOwnerName)
				|| cardOwnerName.trim().split("\\s+").length < 2) {
			errorList.add(ERROR_CARD_OWNER_NAME);
		}
	}

	private static void validateUserId(Long userId, List<String> errorList) {
		if (userId == null || userId <= 0) {
			errorList.add(ERROR_USER_ID);
		}
	}

	private static void validateSubscriptionId(Long subscriptionId, List<String> errorList) {
		if (subscriptionId == null || subscriptionId <= 0) {
			errorList.add(ERROR_SUBSCRIPTION_ID);
		}
	}
}
