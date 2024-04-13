package com.coen6312.ecommerce;

import com.coen6312.ecommerce.entity.EcommerceSystem;
import com.coen6312.ecommerce.service.BuyerService;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import com.coen6312.ecommerce.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BuyerServiceTest {
    private final EcommerceSystem system = new EcommerceSystem();
    private final EcommerceSystemService systemService = new EcommerceSystemService(system);
    private final ShoppingCartService shoppingCartService = new ShoppingCartService(systemService);

    private final BuyerService buyerService = new BuyerService(systemService, shoppingCartService);

    @BeforeEach
    void setUp() {
        system.initialize();
    }

}
