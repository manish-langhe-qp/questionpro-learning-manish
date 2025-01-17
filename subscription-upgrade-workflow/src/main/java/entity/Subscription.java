package entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class Subscription {

	@Column(nullable = false, unique = true)
	private Long subscriptionId;

	@Column(nullable = false)
	private LocalDateTime updationTs;

	@Column(nullable = false)
	private LocalDateTime expirationTs;

	@Column(nullable = false)
	private String status; // PENDING_PAYMENT, ACTIVE, SUSPENDED, etc.

	@Column(nullable = false)
	private boolean autoRenew;
	
	@Column(nullable = false)
	private String transactionID; // 

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getupdationTs() {
		return updationTs;
	}

	public void setupdationTs(LocalDateTime updationTs) {
		this.updationTs = updationTs;
	}

	public LocalDateTime getExpirationTs() {
		return expirationTs;
	}

	public void setExpirationTs(LocalDateTime expirationTs) {
		this.expirationTs = expirationTs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	// Utility methods for subscription status
	public boolean isPendingPayment() {
		return "PENDING_PAYMENT".equalsIgnoreCase(status);
	}

	public boolean isExpiringSoon() {
		LocalDateTime now = LocalDateTime.now();
		return expirationTs.isAfter(now) && expirationTs.isBefore(now.plusDays(7)); // Expiring within 7 days
	}

	public boolean isOverdue() {
		return expirationTs.isBefore(LocalDateTime.now()) && "ACTIVE".equalsIgnoreCase(status);
	}

}
