package com.coen6312.ecommerce.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
public class EcommerceSystem {
	private HashMap<String, Product> products = new HashMap<>();
	private HashMap<String, User> users = new HashMap<>();
	private HashMap<String, Category> categories = new HashMap<>();

	public void initialize() {
		// Reset or initialize the state before each test
		this.products = new HashMap<>();
		this.users = new HashMap<>();
	}
	public EcommerceSystem() {
		seedData(); // Populate with dummy data upon instantiation
	}

	private void seedData() {
		// Initializing categories first
		Category category1 = new Category("c1", "Sneakers");
		Category category2 = new Category("c2", "Boots");
		Category category3 = new Category("c3", "Walking Shoes");
		Category category4 = new Category("c4", "Running Shoes");

		this.categories.put("c1", category1);
		this.categories.put("c2", category2);
		this.categories.put("c3", category3);
		this.categories.put("c4", category4);

		// Example dummy products with corrected category references
		Product product1 = Product.builder()
				.productId("p1")
				.sellerId("s1")
				.productName("Sneaker XYZ")
				.unitPrice(99.99)
				.productDescription("The latest in sneaker technology")
				.productSize(7)
				.quantity(100)
				.categoryId("c1")
				.brand("XYZ Sneakers")
				.build();

		Product product2 = Product.builder()
				.productId("p2")
				.sellerId("s2")
				.productName("Outdoor Adventure Boots")
				.unitPrice(149.99)
				.productDescription("Durable boots for any terrain")
				.productSize(8)
				.quantity(50)
				.categoryId("c2")
				.brand("Adventure Gear")
				.build();

		Product product3 = Product.builder()
				.productId("p3")
				.sellerId("s3")
				.productName("Comfortable Walking Shoes")
				.unitPrice(79.99)
				.productDescription("Perfect shoes for everyday comfort")
				.productSize(9)
				.quantity(75)
				.categoryId("c3")
				.brand("Comfort Walk")
				.build();

		Product product4 = Product.builder()
				.productId("p4")
				.sellerId("s1")
				.productName("Lightweight Running Shoes")
				.unitPrice(89.99)
				.productDescription("Lightweight shoes for runners who want speed")
				.productSize(10)
				.quantity(1)
				.categoryId("c4")
				.brand("SpeedRun")
				.build();

		this.products.put("p1", product1);
		this.products.put("p2", product2);
		this.products.put("p3", product3);
		this.products.put("p4", product4);

		// Example dummy users
		User admin = Admin.builder().id("a1").userName("Admin").build();
		User buyer1 = Buyer.builder().id("b1").userName("Bob").build();
		User seller1 = Seller.builder().id("s1").userName("John").build();

		this.users.put("a1", admin);
		this.users.put("b1", buyer1);
		this.users.put("s1", seller1);
	}
}