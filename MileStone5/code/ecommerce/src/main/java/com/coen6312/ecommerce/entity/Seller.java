package com.coen6312.ecommerce.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Seller extends User {
	private String shopName;
	@Builder.Default
	private int totalQuantity = 0;
	private String description;
	@Builder.Default
	private int complaintTimes = 0;
}