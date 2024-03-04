package com.coen6312.ecommerce.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Buyer extends User {

	private List<Address> addresses;
	private HashMap<String, Order> orders;
	private ShoppingCart shoppingCart;

}