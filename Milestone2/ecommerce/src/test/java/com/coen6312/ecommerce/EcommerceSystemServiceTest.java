package com.coen6312.ecommerce;

import com.coen6312.ecommerce.entity.*;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EcommerceSystemServiceTest {
	private final EcommerceSystem system = new EcommerceSystem();
	private final EcommerceSystemService service = new EcommerceSystemService(system);

	@BeforeEach
	public void setUp() {
		system.initialize();
	}

	@Test
	void addUserShouldReturnTrue() {
		User newUser = User.builder().userName("John").id("u1").build();
		assertTrue(service.addUser(newUser));
		assertTrue(system.getUsers().containsKey("u1"));
		assertEquals(newUser, system.getUsers().get("u1"));
	}

	@Test
	void findProductByIdShouldReturnProduct() {
		Product product = Product.builder().productId("p1")
				.sellerId("s1")
				.productName("Nike Sneaker")
				.unitPrice(100.0)
				.productDescription("Cool")
				.productSize(8)
				.quantity(20)
				.brand("Nike")
				.categoryId("c1").build();
		system.getProducts().put(product.getProductId(), product);
		assertEquals(product, system.getProducts().get("p1"));
	}

	@Test
	void findUserByIdShouldReturnUser() {
		User newUser = User.builder().userName("Tom").id("u1").build();
		service.addUser(newUser);
		assertEquals(newUser, service.findUserById("u1"));
	}

	@Test
	void checkOutOfStockProductsShouldReturnEmptyIfAllInStock() {
		Product product = Product.builder().productId("p2")
				.sellerId("s1")
				.productName("Nike Sneaker")
				.unitPrice(100.0)
				.productDescription("Cool")
				.productSize(8)
				.quantity(0)
				.brand("Nike")
				.categoryId("c1").build();
		system.getProducts().put(product.getProductId(), product);
		assertEquals(1, service.checkOutOfStockProducts().size());
	}

	@Test
	void informTheSellerAndSellerShouldGetMessage() throws Exception {
        Seller seller = Seller.builder()
                .shopName("Nike")
                .description("Great")
                .userName("sellerName")
                .id("s1")
                .build();
		service.addUser(seller);
		Product product = Product.builder().productId("p1")
				.sellerId("s1")
				.productName("Nike Sneaker")
				.unitPrice(100.0)
				.productDescription("Cool")
				.productSize(8)
				.quantity(0)
				.brand("Nike")
				.categoryId("c1").build();
		system.getProducts().put(product.getProductId(), product);
		service.informTheSeller(seller.getId(), product.getProductId());
		assertEquals(Message.MessageType.OUT_OF_STOCK, seller.getNotifications().get(0).getType());
	}
}