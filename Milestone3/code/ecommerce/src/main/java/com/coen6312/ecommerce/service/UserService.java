package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.Product;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.List;

@Service
public class UserService {
    private final EcommerceSystemService systemService;

    public UserService(EcommerceSystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * View the feedbacks of the product by productId
     * @param productId product id
     */
    public List<String> viewFeedbacks(String productId) throws Exception {
        // Use the instance to call findProductById
        Product product = systemService.findProductById(productId);

        if (product != null) {
            // Assuming Product has a method to get feedbacks, replace "getFeedbacks()" with the actual method name
            return product.getFeedbacks(); // This line is conceptual; adjust according to your Product class's feedback retrieval method
        } else {
            // Return an empty array or some indication that the product was not found or has no feedbacks
            throw new Exception("Product was not found");
        }
    }
}
