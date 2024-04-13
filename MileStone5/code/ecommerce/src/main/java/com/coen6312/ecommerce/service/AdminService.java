package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final EcommerceSystemService systemService;

    public AdminService(EcommerceSystemService systemService) {
        this.systemService = systemService;
    }

    public HashMap<String, Product> viewAllProducts() {
        return systemService.getProducts();
    }

    /**
     * Block the seller by id.
     * @param sellerId seller id
     */
    public boolean blockSeller(String sellerId) {
        if(this.systemService.getUsers().get(sellerId) != null){
            this.systemService.getUsers().remove(sellerId);
            return true;
        }
        return false;
    }

    public boolean sendWarnings(String sellerId, String warningMessage) {
        User user = systemService.findUserById(sellerId);
        if (user instanceof Seller) {
            Seller seller = (Seller) user;
            Message warning = new Message(Message.MessageType.WARNING, warningMessage);
            seller.getNotifications().add(warning);
            return true;
        }
        return false;
    }

    public boolean doesAdminExists(String adminId) {
        User user = systemService.getUsers().get(adminId);
        return user instanceof Admin;
    }

    public List<Message> checkNotifications(String adminId, Message.MessageType messageType) {
        Admin admin = (Admin) systemService.findUserById(adminId);
        if (admin != null) {
            // Filter notifications by MessageType
            return admin.getNotifications().stream()
                    .filter(message -> message.getType().equals(messageType))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Admin not found");
        }
    }
}
