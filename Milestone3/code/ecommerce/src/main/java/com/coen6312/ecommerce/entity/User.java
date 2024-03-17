package com.coen6312.ecommerce.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
public class User {
	private final String id;
	private final String userName;
	@Builder.Default
	private List<Message> notifications = new ArrayList<>();
}