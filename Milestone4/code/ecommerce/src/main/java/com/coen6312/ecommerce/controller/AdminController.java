package com.coen6312.ecommerce.controller;

import com.coen6312.ecommerce.entity.Message;
import com.coen6312.ecommerce.entity.Product;
import com.coen6312.ecommerce.service.AdminService;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Controller
public class AdminController {
    private final AdminService adminService;
    private final EcommerceSystemService ecommerceSystemService;

    public AdminController(AdminService adminService, EcommerceSystemService ecommerceSystemService) {
        this.adminService = adminService;
        this.ecommerceSystemService = ecommerceSystemService;
    }

    public void viewAllProducts() throws Exception {
        System.out.println("Viewing all products...");
        HashMap<String, Product> products = adminService.viewAllProducts();
        if (products.isEmpty()) {
            System.out.println("There are no products in the system.");
        } else {
            products.forEach((id, product) -> System.out.println("ID: " + id + ", Name: " + product.getProductName() + ", Price: $" + product.getUnitPrice()));
        }
    }

    public void blockSeller(BufferedReader reader) throws Exception {
        System.out.println("Enter the ID of the seller you wish to block:");
        String sellerId = reader.readLine();
        boolean success = adminService.blockSeller(sellerId);
        if (success) {
            System.out.println("Seller " + sellerId + " has been blocked.");
        } else {
            System.out.println("Failed to block seller " + sellerId + ". They may not exist.");
        }
    }

    public void sendWarnings(BufferedReader reader) throws Exception {
        System.out.println("Enter the ID of the seller you wish to send a warning to:");
        String sellerId = reader.readLine();
        System.out.println("Enter your warning message:");
        String warningMessage = reader.readLine();

        boolean success = adminService.sendWarnings(sellerId, warningMessage);
        if (success) {
            System.out.println("Warning sent successfully to seller: " + sellerId);
        } else {
            System.out.println("Failed to send warning. Please make sure the seller ID is correct and try again.");
        }
    }

    public void viewNotifications(BufferedReader reader) throws IOException {
        System.out.println("Enter Admin ID:");
        String adminId = reader.readLine();
        // Validate if the seller ID exists
        if (!adminService.doesAdminExists(adminId)) {
            System.out.println("Invalid ID. Please try again.");
            return;
        }
        List<Message> notifications = adminService.checkNotifications(adminId, Message.MessageType.WARNING_NEEDED);
        if (notifications.isEmpty()) {
            System.out.println("No notifications from Customer Service Representatives.");
        } else {
            System.out.println("Notifications from Customer Service Representatives:");
            for (Message notification : notifications) {
                System.out.println("- " + notification.getContent());
            }
        }
    }
}
