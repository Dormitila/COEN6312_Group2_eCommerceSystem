package com.coen6312.ecommerce.controller;

import com.coen6312.ecommerce.entity.Message;
import com.coen6312.ecommerce.service.CSRService;
import com.coen6312.ecommerce.service.SellerService;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.util.List;

@Controller
public class CSRController {
    private final CSRService csrService;
    private final SellerService sellerService;

    public CSRController(CSRService csrService, SellerService sellerService) {
        this.csrService = csrService;
        this.sellerService = sellerService;
    }

    public void viewComplaints(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }
        List<Message> complaints = csrService.viewComplaints(sellerId);
        if (complaints.isEmpty()) {
            System.out.println("No complaints.");
        } else {
            for (Message complaint : complaints) {
                System.out.println("- " + complaint.getContent());
            }
        }
    }

    public void escalateComplaints(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID you want to escalate complaints for:");
        String sellerId = reader.readLine();

        System.out.println("Enter Admin ID you want to escalate to:");
        String adminId = reader.readLine();

        boolean success = csrService.escalateComplaints(sellerId, adminId);

        if (success) {
            System.out.println("Complaint against Seller ID: " + sellerId + " has been escalated to admin successfully.");
        } else {
            System.out.println("Failed to escalate complaint. Please ensure the seller ID is correct and try again.");
        }
    }

    public void resolveIssues(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID you want to resolve complaints for:");
        String sellerId = reader.readLine();

        boolean success = csrService.resolveIssues(sellerId);

        if (success) {
            System.out.println("Complaint against Seller ID: " + sellerId + " has been resolved successfully.");
        } else {
            System.out.println("Failed to resolve complaint. Please ensure the seller ID is correct and try again.");
        }
    }

}
