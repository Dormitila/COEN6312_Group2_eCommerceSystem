package com.coen6312.ecommerce.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PremiumBuyer extends Buyer {

	@Builder.Default
	private double monthlyDiscount = 0.9;
	private Date activationDate;
	private Date expiryDate;
}