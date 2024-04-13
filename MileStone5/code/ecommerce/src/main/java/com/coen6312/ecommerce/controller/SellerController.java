package com.coen6312.ecommerce.controller;

import com.coen6312.ecommerce.entity.Message;
import com.coen6312.ecommerce.entity.Product;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import com.coen6312.ecommerce.service.SellerService;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.util.List;

@Controller
public class SellerController {
    private final SellerService sellerService;
    private final EcommerceSystemService ecommerceSystemService;

    public SellerController(SellerService sellerService, EcommerceSystemService ecommerceSystemService) {
        this.sellerService = sellerService;
        this.ecommerceSystemService = ecommerceSystemService;
    }

    public void addNewProduct(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }

        boolean validProductId = false;
        String productId = "";
        while (!validProductId) {
            System.out.println("Product ID:");
            productId = reader.readLine(); // Get product ID upfront

            Product existingProduct = ecommerceSystemService.findProductById(productId);
            if (existingProduct != null && existingProduct.getSellerId().equals(sellerId)) {
                System.out.println("Product already exists. Do you want to add quantity to this product? (yes/no):");
                String response = reader.readLine();
                if ("yes".equalsIgnoreCase(response)) {
                    System.out.println("Enter additional quantity:");
                    int additionalQuantity = Integer.parseInt(reader.readLine());
                    existingProduct.setQuantity(existingProduct.getQuantity() + additionalQuantity);
                    sellerService.updateProductById(productId, existingProduct, sellerId);
                    System.out.println("Product quantity updated successfully.");
                    return;
                } else {
                    System.out.println("Please enter a different Product ID.");
                }
            } else {
                validProductId = true; // Product ID is either new or not tied to this seller
            }
        }

        // Continue to gather details for a new product
        System.out.println("Adding a new product...");
        System.out.println("Product Name:");
        String productName = reader.readLine();

        System.out.println("Unit Price:");
        double unitPrice = Double.parseDouble(reader.readLine());

        System.out.println("Description:");
        String description = reader.readLine();

        System.out.println("Product Size:");
        int size = Integer.parseInt(reader.readLine());

        System.out.println("Quantity:");
        int quantity = Integer.parseInt(reader.readLine());

        System.out.println("Category ID:");
        String categoryId = reader.readLine();

        System.out.println("Brand:");
        String brand = reader.readLine();

        Product newProduct = Product.builder()
                .productId(productId)
                .sellerId(sellerId)
                .productName(productName)
                .unitPrice(unitPrice)
                .productDescription(description)
                .productSize(size)
                .quantity(quantity)
                .categoryId(categoryId)
                .brand(brand)
                .build();

        boolean productAdded = sellerService.addProduct(newProduct);
        if (productAdded) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product. Please check the details and try again.");
        }
    }

    public void checkMyStock(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();

        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }

        System.out.println("Listing your products...");
        List<Product> products = sellerService.checkMyStock(sellerId);
        if (products.isEmpty()) {
            System.out.println("No products found for your Seller ID.");
        } else {
            products.forEach(product -> {
                System.out.println("Product ID: " + product.getProductId() +
                        ", Name: " + product.getProductName() +
                        ", Price: $" + product.getUnitPrice() +
                        ", Quantity: " + product.getQuantity() +
                        ", Size: " + product.getProductSize() +
                        ", Description: " + product.getProductDescription());
            });
        }
    }

    public void updateProductById(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();

        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }

        System.out.println("Enter Product ID to update:");
        String productId = reader.readLine();

        // Fetch existing product details
        Product product = ecommerceSystemService.findProductById(productId);
        if (product == null || !product.getSellerId().equals(sellerId)) {
            System.out.println("Product not found or does not belong to you.");
            return;
        }

        System.out.println("Current Product Name: " + product.getProductName());
        System.out.println("Enter new Product Name (leave blank to keep current):");
        String productName = reader.readLine();
        if (!productName.isBlank()) {
            product.setProductName(productName);
        }

        System.out.println("Current Unit Price: " + product.getUnitPrice());
        System.out.println("Enter new Unit Price (leave blank to keep current):");
        String unitPriceStr = reader.readLine();
        if (!unitPriceStr.isBlank()) {
            double unitPrice = Double.parseDouble(unitPriceStr);
            product.setUnitPrice(unitPrice);
        }

        System.out.println("Current Description: " + product.getProductDescription());
        System.out.println("Enter new Description (leave blank to keep current):");
        String description = reader.readLine();
        if (!description.isBlank()) {
            product.setProductDescription(description);
        }

        System.out.println("Current Product Size: " + product.getProductSize());
        System.out.println("Enter new Product Size (leave blank to keep current):");
        String sizeStr = reader.readLine();
        if (!sizeStr.isBlank()) {
            int size = Integer.parseInt(sizeStr);
            product.setProductSize(size);
        }

        System.out.println("Current Quantity: " + product.getQuantity());
        System.out.println("Enter new Quantity (leave blank to keep current):");
        String quantityStr = reader.readLine();
        if (!quantityStr.isBlank()) {
            int quantity = Integer.parseInt(quantityStr);
            product.setQuantity(quantity);
        }

        System.out.println("Current Brand: " + product.getBrand());
        System.out.println("Enter new Brand (leave blank to keep current):");
        String brand = reader.readLine();
        if (!brand.isBlank()) {
            product.setBrand(brand);
        }

        System.out.println("Current Category ID: " + product.getCategoryId());
        System.out.println("Enter new Category ID (leave blank to keep current):");
        String categoryId = reader.readLine();
        if (!categoryId.isBlank()) {
            product.setCategoryId(categoryId);
        }

        // Assuming SellerService.updateProductById() method is properly implemented to handle the update
        boolean updated = sellerService.updateProductById(productId, product, sellerId);
        if (updated) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product.");
        }
    }

    public void removeProductById(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }
        System.out.println("Enter Product ID to remove:");
        String productId = reader.readLine();
        boolean removed = sellerService.removeProductById(productId, sellerId);
        if (removed) {
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Failed to remove product or product does not belong to you.");
        }
    }

    public void viewComplaintsFromBuyer(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }
        List<Message> complaints = sellerService.checkNotifications(sellerId, Message.MessageType.COMPLAINT);
        if (complaints.isEmpty()) {
            System.out.println("No complaints from buyers.");
        } else {
            System.out.println("Complaints from buyers:");
            for (Message complaint : complaints) {
                System.out.println("- " + complaint.getContent());
            }
        }
    }

    public void viewWarningsFromAdmin(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }

        List<Message> warnings = sellerService.checkNotifications(sellerId, Message.MessageType.WARNING);
        if (warnings.isEmpty()) {
            System.out.println("No warnings from admin.");
        } else {
            System.out.println("Warnings from Admin:");
            for (Message warning : warnings) {
                System.out.println("Warning: " + warning.getContent());
            }
        }
    }

    public void viewOutOfStockMessages(BufferedReader reader) throws Exception {
        System.out.println("Enter Seller ID:");
        String sellerId = reader.readLine();
        // Validate if the seller ID exists
        if (!sellerService.doesSellerIdExist(sellerId)) {
            System.out.println("Invalid Seller ID. Please try again.");
            return;
        }

        List<Message> outOfStockMessages = sellerService.checkNotifications(sellerId, Message.MessageType.OUT_OF_STOCK);
        if (outOfStockMessages.isEmpty()) {
            System.out.println("No out of stock messages.");
        } else {
            System.out.println("Out of Stock Messages:");
            for (Message message : outOfStockMessages) {
                System.out.println("Message: " + message.getContent());
            }
        }
    }
}