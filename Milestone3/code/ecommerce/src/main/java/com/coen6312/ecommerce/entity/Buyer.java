package com.coen6312.ecommerce.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Buyer extends User {
	@Builder.Default
	private List<Address> addresses = new ArrayList<>();
	@Builder.Default
	private HashMap<String, Order> orders = new HashMap<>();
	@Builder.Default
	private ShoppingCart shoppingCart = new ShoppingCart();
}