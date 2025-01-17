package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpgradeSusbcriptionResponse {
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("transaction_id")
	private String transactionId;
	
	@JsonProperty("error_message")
	private String error;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
