package com.coen6312.ecommerce.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Product {

	private String productId;
	private String sellerId;
	private String productName;
	private double unitPrice;
	private String productDescription;
	private int productSize;
	private int quantity;
	private String brand;
	private String categoryId;
	@Builder.Default
	private List<String> feedbacks = new ArrayList<>();
}