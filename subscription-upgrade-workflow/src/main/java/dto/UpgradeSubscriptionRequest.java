package dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpgradeSubscriptionRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subscriptionID;
	
	@Id
	@NotNull(message = "Plan ID is required.")
	private Long planId;

	@NotBlank(message = "Name on card is required.")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Name on card must only contain letters and spaces.")
	private String name;

	@NotBlank(message = "Card number is required.")
	@Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits.")
	private String cardNumber;

	@NotBlank(message = "CVV is required.")
	@Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits.")
	private String cvv;

	@NotBlank(message = "Expiry date is required.")
	@Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Expiry date must be in MM/YY format.")
	private String expiryDate;
	
	@NotNull(message = "User ID is required.")
	private Long user_id;
	

	public Long getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(Long subscriptionID) {
		this.subscriptionID = subscriptionID;
	}
	
	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

}
