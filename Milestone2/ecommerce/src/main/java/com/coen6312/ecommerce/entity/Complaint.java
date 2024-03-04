package com.coen6312.ecommerce.entity;

import lombok.Data;

@Data
public class Complaint {

	private String complaintId;
	private String productId;
	private String content;
	private String buyerId;

}