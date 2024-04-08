package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.Admin;
import com.coen6312.ecommerce.entity.Message;
import com.coen6312.ecommerce.entity.Seller;
import com.coen6312.ecommerce.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSRService {
    private final EcommerceSystemService systemService;

    public CSRService(EcommerceSystemService systemService) {
        this.systemService = systemService;
    }

    /** View complaints by buyerId
     * @param sellerId
     */
    public List<Message> viewComplaints(String sellerId) {
        Seller seller = (Seller) systemService.findUserById(sellerId);
        if (seller != null) {
            // Filter notifications by MessageType
            return seller.getNotifications().stream()
                    .filter(message -> message.getType().equals(Message.MessageType.COMPLAINT))
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Seller not found");
        }
    }

    /**
     * send notifications to Admin
     * @param sellerId
     * @param adminId
     */
    public boolean escalateComplaints(String sellerId, String adminId) {
        Seller seller = (Seller) systemService.findUserById(sellerId);
        User admin = systemService.findUserById(adminId);

        if (seller == null) {
            return false; // Seller not found
        }

        if (!(admin instanceof Admin)) {
            return false; // Admin not found or not an Admin instance
        }

        String content = "Warning needed for seller: " + sellerId;
        Message notification = new Message(Message.MessageType.WARNING_NEEDED, content);
        ((Admin)admin).getNotifications().add(notification);

        return true;
    }

    public boolean resolveIssues(String sellerId) {
        Seller seller = (Seller) systemService.findUserById(sellerId);

        if (seller == null) {
            return false; // Seller not found
        }

        // remove seller complaints
        List<Message> updatedNotifications = seller.getNotifications().stream()
                .filter(notification -> notification.getType() != Message.MessageType.COMPLAINT)
                .collect(Collectors.toList());

        seller.setNotifications(updatedNotifications);

        return true;
    }
}
