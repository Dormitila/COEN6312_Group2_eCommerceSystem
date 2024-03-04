package com.coen6312.ecommerce;

import com.coen6312.ecommerce.entity.*;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import com.coen6312.ecommerce.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SellerServiceTest {
    private final EcommerceSystem system = new EcommerceSystem();
    private final EcommerceSystemService systemService = new EcommerceSystemService(system);
    private final SellerService sellerService = new SellerService(systemService);

    @BeforeEach
    void setUp() {
        system.initialize();
    }

    @Test
    void addProductShouldReturnTrueWhenProductIsNewAndSellerExists() {
        Seller seller = Seller.builder()
                .shopName("Nike")
                .description("Great")
                .userName("sellerName")
                .id("s1")
                .build();
        systemService.addUser(seller);
        Product product = Product.builder().productId("p1")
                .sellerId(seller.getId())
                .productName("Nike Sneaker")
                .unitPrice(100.0)
                .productDescription("Cool")
                .productSize(8)
                .quantity(20)
                .brand("Nike")
                .categoryId("c1").build();
        boolean result = sellerService.addProduct(product);
        assertTrue(result);
        assertTrue(systemService.getProducts().containsKey("p1"));
        assertEquals(product, systemService.getProducts().get("p1"));
        assertFalse(sellerService.addProduct(product));
    }

    @Test
    void checkMyStockShouldReturnOnlySellerProducts() {
        Product product1 = Product.builder().productId("p1")
                .sellerId("s1")
                .productName("Nike Sneaker")
                .unitPrice(100.0)
                .productDescription("Cool")
                .productSize(8)
                .quantity(20)
                .brand("Nike")
                .categoryId("c1").build();
        Product product2 = Product.builder().productId("p2")
                .sellerId("s2")
                .productName("Converse shoes")
                .unitPrice(50.0)
                .productDescription("Nice")
                .productSize(8)
                .quantity(26)
                .brand("Converse")
                .categoryId("c1").build();
        HashMap<String, Product> productsMap = systemService.getProducts();
        productsMap.put(product1.getProductId(), product1);
        productsMap.put(product2.getProductId(), product2);

        List<Product> products = sellerService.checkMyStock("s1");
        assertEquals(1, products.size());
        assertTrue(products.contains(product1));
    }

    @Test
    void updateProductByIdShouldUpdateProductWhenSellerMatches() {
        Product product1 = Product.builder().productId("p1")
                .sellerId("s1")
                .productName("Nike Sneaker")
                .unitPrice(100.0)
                .productDescription("Cool")
                .productSize(8)
                .quantity(20)
                .brand("Nike")
                .categoryId("c1").build();
        Product product2 = Product.builder().productId("p2")
                .sellerId("s2")
                .productName("Converse shoes")
                .unitPrice(50.0)
                .productDescription("Nice")
                .productSize(8)
                .quantity(26)
                .brand("Converse")
                .categoryId("c1").build();

        HashMap<String, Product> productsMap = systemService.getProducts();
        productsMap.put(product1.getProductId(), product1);
        productsMap.put(product2.getProductId(), product2);;

        Product updatedProduct1 = product1;
        updatedProduct1.setQuantity(19);


        assertTrue(sellerService.updateProductById("p1", updatedProduct1, "s1"));
        assertFalse(sellerService.updateProductById("p1", updatedProduct1, "s2"));
        assertEquals(updatedProduct1, systemService.getProducts().get(product1.getProductId()));
    }

    @Test
    void removeProductByIdShouldRemoveProductWhenSellerMatches() {
        Product product1 = Product.builder().productId("p1")
                .sellerId("s1")
                .productName("Nike Sneaker")
                .unitPrice(100.0)
                .productDescription("Cool")
                .productSize(8)
                .quantity(20)
                .brand("Nike")
                .categoryId("c1").build();

        systemService.getProducts().put(product1.getProductId(), product1);

        assertTrue(sellerService.removeProductById("p1", product1.getSellerId()));
        assertFalse(sellerService.removeProductById("p1", "s2"));
        assertTrue(systemService.getProducts().isEmpty());
    }

    @Test
    void checkNotificationsShouldReturnFilteredNotifications() {
        Seller seller = Seller.builder()
                .shopName("Nike")
                .description("Great")
                .userName("sellerName")
                .id("s1")
                .build();
        systemService.addUser(seller);
        seller.getNotifications().add(new Message(Message.MessageType.OUT_OF_STOCK, "You have a out of stock message"));
        seller.getNotifications().add(new Message(Message.MessageType.COMPLAINT, "You have a complaint"));
        List<Message> result = sellerService.checkNotifications(seller.getId(), Message.MessageType.OUT_OF_STOCK);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(Message.MessageType.OUT_OF_STOCK, result.get(0).getType());
    }
}
