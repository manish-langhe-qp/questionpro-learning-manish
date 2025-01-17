package validator;

import java.time.YearMonth;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import dto.UpgradeSubscriptionRequest;
import exception.BadRequestException;

@Component
public class UserSubscriptionValidator {

	public static void validateUserSubscription(UpgradeSubscriptionRequest paymentRequest) {
		validateName(paymentRequest.getName());
		validateCardNumber(paymentRequest.getCardNumber());
		validateCVV(paymentRequest.getCvv());
		validateExpirationDate(paymentRequest.getExpiryDate());
		validateUserId(paymentRequest.getUser_id());
		validateSubscriptionId(paymentRequest.getSubscriptionID());
	}

	private static void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new BadRequestException("Please enter a valid Name on card.");
		}

		String regex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
		if (!name.matches(regex)) {
			throw new BadRequestException("Please enter a valid Name on card.");
		}

		String[] words = name.trim().split("\\s+");
		if (words.length < 2) {
			throw new BadRequestException("Name on card must have at least two parts (e.g., First Last).");
		}
	}

	private static void validateCardNumber(String cardNumber) {
		if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
			throw new BadRequestException("Card number must be 16 digits.");
		}
	}

	private static void validateCVV(String cvv) {
		if (cvv == null || !cvv.matches("^\\d{3}$")) {
			throw new BadRequestException("CVV must be 3 digits.");
		}
	}

	private static void validateExpirationDate(String expiryDate) {
		if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
			throw new BadRequestException("Expiry date must be in MM/YY format.");
		}

		String[] parts = expiryDate.split("/");
		int month = Integer.parseInt(parts[0]);
		int year = 2000 + Integer.parseInt(parts[1]);

		YearMonth currentYearMonth = YearMonth.now();
		YearMonth cardYearMonth = YearMonth.of(year, month);
		if (cardYearMonth.isBefore(currentYearMonth)) {
			throw new BadRequestException("Card has expired.");
		}
	}

	private static void validateUserId(Long userId) {
		if (userId == null || userId <= 0) {
			throw new BadRequestException("User ID must be a valid positive number.");
		}
	}

	private static void validateSubscriptionId(Long subscriptionId) {
		if (subscriptionId == null || subscriptionId <= 0) {
			throw new BadRequestException("Subscription ID must be a valid positive number.");
		}
	}
}
