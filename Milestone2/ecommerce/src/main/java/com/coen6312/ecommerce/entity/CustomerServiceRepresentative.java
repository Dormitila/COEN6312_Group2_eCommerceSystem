package com.coen6312.ecommerce.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CustomerServiceRepresentative extends User {
	/**
	 *
	 * @param buyerId
	 */
	public Complaint viewComplaints(String buyerId) {
		// TODO - implement com.coen6312.proj.model.CustomerServiceRepresentative.viewComplaints
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param sellerId
	 */
	public void escalateComplaints(String sellerId) {
		// TODO - implement com.coen6312.proj.model.CustomerServiceRepresentative.escalateComplaints
		throw new UnsupportedOperationException();
	}
}