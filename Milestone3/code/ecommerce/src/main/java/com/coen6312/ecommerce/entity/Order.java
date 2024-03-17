package com.coen6312.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Order {

	private String orderId;
	private List<Product> orderItems;
	private Date orderDate;
	private String buyerId;
	private double totalPrice;

}