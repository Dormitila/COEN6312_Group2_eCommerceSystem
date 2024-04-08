package com.coen6312.ecommerce.controller;

import com.coen6312.ecommerce.entity.*;
import com.coen6312.ecommerce.service.BuyerService;
import com.coen6312.ecommerce.service.EcommerceSystemService;
import com.coen6312.ecommerce.service.ShoppingCartService;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
public class BuyerController {
    private final BuyerService buyerService;
    private final EcommerceSystemService ecommerceSystemService;
    private final ShoppingCartService shoppingCartService;

    public BuyerController(BuyerService buyerService, EcommerceSystemService ecommerceSystemService, ShoppingCartService shoppingCartService1) {
        this.buyerService = buyerService;
        this.ecommerceSystemService = ecommerceSystemService;
        this.shoppingCartService = shoppingCartService1;
    }

    public void displayAllItems(BufferedReader reader) {
        HashMap<String, Product> products = buyerService.viewAllIterms();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("Available Products:");
            products.forEach((id, product) -> {
                System.out.println("ID: " + product.getProductId() + ", Name: " + product.getProductName() + ", Price: " + product.getUnitPrice());
            });
        }
    }

    public void searchAndDisplayShoesByFilter(BufferedReader reader) throws Exception {
        Filter filter = new Filter();

        System.out.println("Enter item name (press enter to skip):");
        String itemName = reader.readLine();
        if (!itemName.isEmpty()) filter.setItemName(itemName);

        System.out.println("Enter item category (press enter to skip):");
        String itemCategory = reader.readLine();
        if (!itemCategory.isEmpty()) filter.setItemCategory(itemCategory);

        System.out.println("Enter item brand (press enter to skip):");
        String itemBrand = reader.readLine();
        if (!itemBrand.isEmpty()) filter.setItemBrand(itemBrand);

        System.out.println("Enter minimum price (press enter to skip):");
        String minPriceStr = reader.readLine();
        if (!minPriceStr.isEmpty() && Integer.parseInt(minPriceStr) >= 0) filter.setMinPrice(Double.parseDouble(minPriceStr));

        System.out.println("Enter maximum price (press enter to skip):");
        String maxPriceStr = reader.readLine();
        if (!maxPriceStr.isEmpty() && Integer.parseInt(minPriceStr) > 0) filter.setMaxPrice(Double.parseDouble(maxPriceStr));

        System.out.println("Enter item size (press enter to skip):");
        String itemSizeStr = reader.readLine();
        if (!itemSizeStr.isEmpty()) filter.setItemSize(Integer.parseInt(itemSizeStr));

        HashMap<String, Product> results = buyerService.searchShoesByFilter(filter);

        if (results.isEmpty()) {
            System.out.println("No products found matching your criteria.");
        } else {
            System.out.println("Found products:");
            results.forEach((id, product) -> {
                System.out.println("ID: " + product.getProductId() + ", Name: " + product.getProductName() + ", Price: " + product.getUnitPrice());
            });
        }
    }
    private String getCategoryNameById(String catId) {
        Category category = ecommerceSystemService.getCategories().get(catId);
        if (category != null) {
            return category.getName();
        } else {
            return "Category not found";
        }
    }

    public void displayProductDetails(BufferedReader reader) throws IOException {
        System.out.println("Enter Product ID:");
        String productId = reader.readLine();
        Product product = buyerService.viewDetailsByID(productId);
        if (product != null) {
            System.out.println("ID: " + productId +
                    ", Name: " + product.getProductName() +
                    ", Price: $" + product.getUnitPrice() +
                    ", Description: " + product.getProductDescription() +
                    ", Size: " + product.getProductSize() +
                    ", Quantity: " + product.getQuantity() +
                    ", Category: " + getCategoryNameById(product.getCategoryId()) +
                    ", Brand: " + product.getBrand());

            if (product.getFeedbacks() != null && !product.getFeedbacks().isEmpty()) {
                System.out.println("Feedbacks:");
                product.getFeedbacks().forEach(feedback -> System.out.println("- " + feedback));
            } else {
                System.out.println("No feedback available for this product.");
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void addShoesToCart(BufferedReader reader) throws Exception {
        System.out.println("Enter Product ID to add to your cart:");
        String productId = reader.readLine();
        System.out.println("Enter your User ID:");
        String userId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(userId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }

        // Fetch product details based on ID (simplified example, adjust as needed)
        Product product = buyerService.viewDetailsByID(productId);
        if (product != null && buyerService.addShoesToCart(product, userId)) {
            System.out.println("Product added to cart successfully.");
        } else {
            System.out.println("Failed to add product to cart. Make sure the product ID and your User ID are correct.");
        }
    }

    public void removeShoesFromCart(BufferedReader reader) throws Exception {
        System.out.println("Enter Product ID to remove from your cart:");
        String productId = reader.readLine();
        System.out.println("Enter your User ID:");
        String userId = reader.readLine();

        if (!buyerService.doesBuyerIdExist(userId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }

        if (buyerService.removeShoesFromCart(productId, userId)) {
            System.out.println("Product removed from cart successfully.");
        } else {
            System.out.println("Failed to remove product from cart. Make sure the product is in your cart.");
        }
    }

    public void viewMyCart(BufferedReader reader) throws Exception {
        System.out.println("Enter your User ID to view your cart:");
        String userId = reader.readLine();

        if (!buyerService.doesBuyerIdExist(userId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }

        ShoppingCart cart = shoppingCartService.findShoppingCartByUserId(userId);
        if (cart == null || cart.getAllItems().isEmpty()) {
            System.out.println("Your shopping cart is empty.");
            return;
        }

        System.out.println("Items in your cart:");
        HashMap<String, Product> items = cart.getAllItems();
        items.forEach((productId, product) -> {
            System.out.println("Product ID: " + productId +
                    ", Name: " + product.getProductName() +
                    ", Price: $" + product.getUnitPrice() +
                    ", Quantity: " + product.getQuantity() +
                    ", Subtotal: $" + (product.getUnitPrice() * product.getQuantity()));
            // If Product includes a 'brand' field, you can also display it here.
        });

        // Assuming ShoppingCart has a method to calculate total price
        shoppingCartService.setTotalPrice(userId);
        System.out.println("Total Price: $" + cart.getTotalPrice());
    }

    public void viewMyOrders(BufferedReader reader) throws Exception {
        System.out.println("Enter your User ID to view your orders:");
        String userId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(userId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }
        HashMap<String, Order> orders = buyerService.viewAllOrders(userId);
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found for your account.");
        } else {
            System.out.println("Your orders:");
            orders.forEach((orderId, order) ->
                    System.out.println("Order ID: " + orderId + ", Order Details: " + order.toString()));
        }
    }

    public void viewOrderByID(BufferedReader reader) throws Exception {
        System.out.println("Enter your User ID:");
        String userId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(userId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }
        System.out.println("Enter Order ID to view details:");
        String orderId = reader.readLine();
        Order order = buyerService.viewOrderByID(orderId, userId);
        if (order != null) {
            System.out.println("Order Details: " + order.toString());
            // Expand this print statement based on what information you want to show about the order
        } else {
            System.out.println("Order not found.");
        }
    }

    public void cartCheckOut(BufferedReader reader) throws Exception {
        System.out.println("Enter your User ID for checkout:");
        String userId = reader.readLine();

        // Validate if the user exists
        User user = ecommerceSystemService.findUserById(userId);
        if (!(user instanceof Buyer)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }

        // Validate if the shopping cart exists and is not empty
        ShoppingCart cart = shoppingCartService.findShoppingCartByUserId(userId);
        if (cart == null || cart.getAllItems().isEmpty()) {
            System.out.println("Your shopping cart is empty.");
            return;
        }

        // Display cart for final confirmation
        viewMyCart(reader);

        System.out.println("Proceed with checkout? (yes/no):");
        String confirmation = reader.readLine();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Checkout canceled.");
            return;
        }

        // Display available payment methods
        System.out.println("Available Payment Methods:");
        PaymentMethod.paymentMethods.forEach((id, method) -> System.out.println("ID: " + id + ", Method: " + method.getMethodName()));
        // Select one payment
        System.out.println("Select Payment Method ID:");
        String paymentId = reader.readLine();
        try {
            boolean checkoutSuccess = buyerService.cartCheckOut(userId, paymentId);
            if (checkoutSuccess) {
                System.out.println("Checkout successful. Thank you for your purchase!");
                if(!buyerService.createNewOrder(userId)){
                    throw new Exception("Create new order failed!");
                };
                buyerService.resetCart(userId);
            } else {
                System.out.println("Checkout failed.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during checkout: " + e.getMessage());
        }
    }

    public void raiseComplaint(BufferedReader reader) throws Exception {
        System.out.println("Enter your Buyer ID:");
        String buyerId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(buyerId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }

        System.out.println("Enter Seller ID you want to complain about:");
        String sellerId = reader.readLine();
        User seller = ecommerceSystemService.findUserById(sellerId);
        if (!(seller instanceof Seller)) {
            System.out.println("Invalid Seller ID or the user is not a seller.");
            return;
        }

        System.out.println("Type your complaint:");
        String complaintText = reader.readLine();
        String fullComplaintText = "Complaint from Buyer ID: " + buyerId +
                " against Seller ID: " + sellerId +
                " - " + complaintText;

        Message complaint = new Message(Message.MessageType.COMPLAINT, fullComplaintText);
        buyerService.raiseComplaint(buyerId, sellerId, complaint);
        System.out.println("Your complaint has been submitted.");
    }

    public void writeFeedback(BufferedReader reader) throws Exception {
        System.out.println("Enter Product ID for feedback:");
        String productId = reader.readLine();

        // Check if the product exists
        Product product = ecommerceSystemService.findProductById(productId);
        if (product == null) {
            System.out.println("Product with ID: " + productId + " does not exist. Please try again.");
            return;
        }

        System.out.println("Write your feedback:");
        String feedback = reader.readLine();

        buyerService.writeFeedBack(productId, feedback);
        System.out.println("Thank you for your feedback!");
    }

    public void viewMyComplaints(BufferedReader reader) throws Exception {
        System.out.println("Enter your Buyer ID to view complaints:");
        String buyerId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(buyerId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }
        List<Message> complaints = buyerService.viewMyComplaints(buyerId);

        if (complaints.isEmpty()) {
            System.out.println("You have not raised any complaints.");
        } else {
            complaints.forEach(complaint -> System.out.println("Complaint: " + complaint.getContent()));
        }
    }

    public void manageAddresses(BufferedReader reader) throws Exception {
        System.out.println("Enter your Buyer ID:");
        String buyerId = reader.readLine();
        if (!buyerService.doesBuyerIdExist(buyerId)) {
            System.out.println("Invalid Buyer ID. Please try again.");
            return;
        }
        boolean exit = false;
        while (!exit) {
            System.out.println("Addresses Management: 1. Add Address 2. Edit Address 3. Remove Address " +
                    "4. View all addresses 5. Exit");
            String option = reader.readLine();
            switch (option) {
                case "1":
                    // Implement add address logic
                    System.out.println("Adding new address.");
                    System.out.println("Enter City:");
                    String city = reader.readLine();
                    System.out.println("Enter State:");
                    String state = reader.readLine();
                    System.out.println("Enter Postal Code:");
                    String postalCode = reader.readLine();
                    String addressId = UUID.randomUUID().toString();
                    // Assuming Address has an appropriate constructor or builder pattern
                    Address newAddress = new Address(addressId, city, state, postalCode);
                    buyerService.addAddress(buyerId, newAddress);
                    System.out.println("Address added successfully.");
                    break;
                case "2":
                    System.out.println("Editing an existing address.");
                    System.out.println("Enter Address ID:");
                    addressId = reader.readLine();

                    if (!buyerService.doesAddressExist(buyerId, addressId)) {
                        System.out.println("Address ID not found. Please try again.");
                        break;
                    }

                    System.out.println("Enter new City (press Enter to skip):");
                    city = reader.readLine();
                    System.out.println("Enter new State (press Enter to skip):");
                    state = reader.readLine();
                    System.out.println("Enter new Postal Code (press Enter to skip):");
                    postalCode = reader.readLine();

                    Address updatedAddress = new Address(addressId, city, state, postalCode);
                    buyerService.editAddress(buyerId, addressId, updatedAddress);
                    System.out.println("Address updated successfully.");
                    break;
                case "3":
                    System.out.println("Removing an address.");
                    System.out.println("Enter Address ID:");
                    addressId = reader.readLine();

                    if (!buyerService.doesAddressExist(buyerId, addressId)) {
                        System.out.println("Address ID not found. Please try again.");
                        break;
                    }

                    buyerService.removeAddress(buyerId, addressId);
                    System.out.println("Address removed successfully.");
                    break;
                case "4":
                    List<Address> addresses = buyerService.viewAllAddresses(buyerId);
                    if (addresses.isEmpty()) {
                        System.out.println("No addresses found.");
                    } else {
                        for (Address address : addresses) {
                            System.out.println("Address ID: " + address.getAddressID() +
                                    ", City: " + address.getCity() +
                                    ", State: " + address.getState() +
                                    ", Postal Code: " + address.getPostalCode());
                        }
                    }
                    break;
                case "5":
                    System.out.println("Exiting Address Management.");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option selected.");
                    break;
            }
        }
    }
}
