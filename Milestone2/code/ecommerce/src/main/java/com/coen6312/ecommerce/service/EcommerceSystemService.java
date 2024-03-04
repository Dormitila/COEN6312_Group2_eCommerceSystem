package com.coen6312.ecommerce.service;


import com.coen6312.ecommerce.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EcommerceSystemService {

    private final EcommerceSystem system;

    public EcommerceSystemService(EcommerceSystem system) {
        this.system = system;
    }

    public HashMap<String, Product> getProducts(){
        return system.getProducts();
    }

    public HashMap<String, User> getUsers(){
        return system.getUsers();
    }


    /**
     * Add a user
     * @param user
     * @return
     */
    public boolean addUser(User user) {
        if (user != null && !getUsers().containsKey(user.getId())) {
            getUsers().put(user.getId(), user);
            return true;
        }
        return false;
    }

    /**
     * Find the product by id.
     * @param productId
     * @return
     */
    public Product findProductById(String productId) {
        return getProducts().get(productId);
    }

    /**
     * Fine the user by id.
     * @param userId
     * @return
     */
    public User findUserById(String userId) {
        return getUsers().get(userId);
    }

    /**
     * Check if the product is out of Stock.
     * @return Out of stock seller ids of the product
     */
    public Collection<String> checkOutOfStockProducts() {
        return system.getProducts().values().stream()
                .filter(product -> product.getQuantity() == 0)
                .map(Product::getSellerId) // Map to seller ID instead of product ID
                .collect(Collectors.toSet());
    }

    /**
     * Inform the seller the product is out of stock.
     * @param sellerId
     * @param productId
     * @throws Exception
     */
    public void informTheSeller(String sellerId, String productId) throws Exception {
        User user = findUserById(sellerId);
        if (user instanceof Seller seller) {
            String content = "Product ID: " + productId + " - is out of stock";
            Message notification  = new Message(Message.MessageType.OUT_OF_STOCK,content);
            ((Seller) user).getNotifications().add(notification);
        } else {
            throw new Exception("Seller with ID " + sellerId + " not found.");
        }
    }
}
