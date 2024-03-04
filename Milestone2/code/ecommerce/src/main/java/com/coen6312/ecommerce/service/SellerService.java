package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.Message;
import com.coen6312.ecommerce.entity.Product;
import com.coen6312.ecommerce.entity.Seller;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final EcommerceSystemService systemService;

    public SellerService(EcommerceSystemService systemService) {
        this.systemService = systemService;
    }

    public boolean addProduct(Product product) {
        Seller seller = (Seller) systemService.findUserById(product.getSellerId());
        if (seller != null && !systemService.getProducts().containsKey(product.getProductId())) {
            systemService.getProducts().put(product.getProductId(), product);
            return true;
        }
        return false;
    }

    public List<Product> checkMyStock(String sellerId) {
        return systemService.getProducts().values().stream()
                .filter(product -> product.getSellerId().equals(sellerId))
                .collect(Collectors.toList());
    }

    public boolean updateProductById(String productId, Product updatedProduct, String sellerId) {
        Product product = systemService.findProductById(productId);
        if (product != null && product.getSellerId().equals(sellerId)) {
            systemService.getProducts().put(productId, updatedProduct);
            return true;
        }
        return false;
    }

    public boolean removeProductById(String productId, String sellerId) {
        Product product = systemService.findProductById(productId);
        if (product != null && product.getSellerId().equals(sellerId)) {
            systemService.getProducts().remove(productId);
            return true;
        }
        return false;
    }

    /**
     * View my complaints or out-of-stock message or warning message
     * @param sellerId
     * @param
     * @return
     */
    public List<Message> checkNotifications(String sellerId, Message.MessageType messageType) {
        Seller seller = (Seller) systemService.findUserById(sellerId);
        if (seller != null) {
            // Filter notifications by MessageType
            return seller.getNotifications().stream()
                    .filter(message -> message.getType().equals(messageType))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Seller not found");
        }
    }

}
