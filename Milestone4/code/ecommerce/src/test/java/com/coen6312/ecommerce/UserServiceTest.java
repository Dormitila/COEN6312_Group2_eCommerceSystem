package com.coen6312.ecommerce;

import com.coen6312.ecommerce.entity.*;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import com.coen6312.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    private final EcommerceSystem system = new EcommerceSystem();
    private final EcommerceSystemService systemService = new EcommerceSystemService(system);
    private final UserService sellerService = new UserService(systemService);

    @BeforeEach
    void setUp() {
        system.initialize();
    }

    @Test
    void viewFeedbacksShouldReturnFeedbacksWhenProductExists() throws Exception {
        Product product = Product.builder().productId("p1")
                .sellerId("s1")
                .productName("Nike Sneaker")
                .unitPrice(100.0)
                .productDescription("Cool")
                .productSize(8)
                .quantity(20)
                .brand("Nike")
                .categoryId("c1").build();
        systemService.getProducts().put(product.getProductId(),product);
        List<String> expectedFeedbacks = List.of("Great product", "Loved it!");
        product.setFeedbacks(expectedFeedbacks);

        List<String> actualFeedbacks = sellerService.viewFeedbacks(product.getProductId());

        assertEquals(expectedFeedbacks, actualFeedbacks);
    }

    @Test
    void viewFeedbacksShouldThrowExceptionWhenProductDoesNotExist() {
        Exception exception = assertThrows(Exception.class, () -> sellerService.viewFeedbacks("p1"));
        assertEquals("Product was not found", exception.getMessage());
    }
}
