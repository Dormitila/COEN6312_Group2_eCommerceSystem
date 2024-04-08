package com.coen6312.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
	private HashMap<String, Product> allItems = new HashMap<>();
	private double totalPrice;
	private String paymentMethod;
}