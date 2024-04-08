package com.coen6312.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {

	private String addressID;
	private String city;
	private String state;
	private String postalCode;

}