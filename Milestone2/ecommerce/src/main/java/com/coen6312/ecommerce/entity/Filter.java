package com.coen6312.ecommerce.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

	private String itemName;
	private String itemCategory;
	private double minPrice;
	private double maxPrice;
	private String itemBrand;
	private double itemSize;

}