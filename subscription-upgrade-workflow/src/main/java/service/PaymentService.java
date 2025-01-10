package service;

import entity.PaymentRequest;
import entity.PaymentResponse;

public interface PaymentService {

	public PaymentResponse processPayment(PaymentRequest paymentRequest);
}
