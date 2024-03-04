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
	private static final Map<String, PaymentMethod> paymentMethods = new HashMap<>();

	// Static block to initialize the collection
	static {
		// Example payment methods
		paymentMethods.put("1", new PaymentMethod("1", "Credit Card"));
		paymentMethods.put("2", new PaymentMethod("2", "PayPal"));
		paymentMethods.put("3", new PaymentMethod("3", "Bitcoin"));
	}

	// Method to fetch a PaymentMethod by its ID
	public static PaymentMethod getPaymentMethodById(String paymentId) {
		return paymentMethods.get(paymentId);
	}
}