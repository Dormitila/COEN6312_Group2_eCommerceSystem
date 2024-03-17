package com.coen6312.ecommerce.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class PaymentMethod {

	private String paymentId;
	private String methodName;

	// Static collection to simulate a database of payment methods
	public static final Map<String, PaymentMethod> paymentMethods = new HashMap<>();

	// Static block to initialize the collection
	static {
		// Example payment methods
		paymentMethods.put("pm1", new PaymentMethod("pm1", "Credit Card"));
		paymentMethods.put("pm2", new PaymentMethod("pm2", "PayPal"));
		paymentMethods.put("pm3", new PaymentMethod("pm3", "Bitcoin"));
	}

	// Method to fetch a PaymentMethod by its ID
	public static PaymentMethod getPaymentMethodById(String paymentId) {
		return paymentMethods.get(paymentId);
	}
}