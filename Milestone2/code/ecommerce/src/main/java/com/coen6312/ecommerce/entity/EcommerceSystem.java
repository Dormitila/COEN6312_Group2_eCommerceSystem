package com.coen6312.ecommerce.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
@NoArgsConstructor
public class EcommerceSystem {
	private HashMap<String, Product> products = new HashMap<>();
	private HashMap<String, User> users = new HashMap<>();

	public void initialize() {
		// Reset or initialize the state before each test
		this.products = new HashMap<>();
		this.users = new HashMap<>();
	}
}