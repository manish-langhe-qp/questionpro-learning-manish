package entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Subscription {

	@Id
	@Column(name = "subscription_id")
	private Long subscriptionId;

	@NotNull(message = "User id is required")
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "creation_ts")
	private LocalDateTime creationTs;

	@Column(name = "expiration_ts")
	private LocalDateTime expirationTs;

	@Column(name = "transaction_id")
	private String transactionid;

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(LocalDateTime creationTs) {
		this.creationTs = creationTs;
	}

	public LocalDateTime getExpirationTs() {
		return expirationTs;
	}

	public void setExpirationTs(LocalDateTime expirationTs) {
		this.expirationTs = expirationTs;
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
}
