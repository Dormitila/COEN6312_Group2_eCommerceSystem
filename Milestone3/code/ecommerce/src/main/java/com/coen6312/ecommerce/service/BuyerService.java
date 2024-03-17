package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    private final EcommerceSystemService systemService;
    private final ShoppingCartService shoppingCartService;

    public BuyerService(EcommerceSystemService systemService, ShoppingCartService shoppingCartService) {
        this.systemService = systemService;
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * Search products by filter
     * @param filter
     * @return Product results
     */
    public HashMap<String, Product> searchShoesByFilter(Filter filter) {
        HashMap<String, Product> allProductsMap = systemService.getProducts(); // Assuming this now returns a HashMap

        return allProductsMap.entrySet().stream().filter(entry -> {
                    Product product = entry.getValue();
                    if (filter.getItemName() != null && !product.getProductName().contains(filter.getItemName())) {
                        return false;
                    }
                    if (filter.getItemCategory() != null && !product.getCategoryId().equals(filter.getItemCategory())) {
                        return false;
                    }
                    if (filter.getItemBrand() != null && !product.getBrand().equals(filter.getItemBrand())) {
                        return false;
                    }
                    if (filter.getMinPrice() > 0 && product.getUnitPrice() < filter.getMinPrice()) {
                        return false;
                    }
                    if (filter.getMaxPrice() > 0 && product.getUnitPrice() > filter.getMaxPrice()) {
                        return false;
                    }
                    if (filter.getItemSize() > 0 && product.getProductSize() != filter.getItemSize()) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
    }

    /**
     * View All products that are on sold.
     * @return
     */
    public HashMap<String, Product> viewAllIterms() {
        return systemService.getProducts();
    }

    /**
     * View the product's details by product id
     * @param productId
     */
    public Product viewDetailsByID(String productId) {
        return systemService.getProducts().get(productId);
    }

    /**
     * Add a product to the shopping cart
     * @param product
     * @param userId
     */
    public boolean addShoesToCart(Product product, String userId) {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        if (shoppingCart == null) {
            return false;
        }

        Product existingProductInCart = shoppingCart.getAllItems().get(product.getProductId());
        Product productInInventory = systemService.findProductById(product.getProductId());

        if (productInInventory == null || productInInventory.getQuantity() <= 0) {
            System.out.println("This product is out of stock and cannot be added to your cart.");
            return false;
        }

        int quantityToAdd = 1; // Default quantity to add is 1
        if (existingProductInCart != null) {
            if (existingProductInCart.getQuantity() + quantityToAdd > productInInventory.getQuantity()) {
                System.out.println("Cannot add more of this product to your cart due to stock limitations.");
                return false;
            } else {
                existingProductInCart.setQuantity(existingProductInCart.getQuantity() + quantityToAdd);
            }
        } else {
            Product productToAdd = Product.builder()
                    .productId(product.getProductId())
                    .sellerId(product.getSellerId())
                    .productName(product.getProductName())
                    .unitPrice(product.getUnitPrice())
                    .productDescription(product.getProductDescription())
                    .productSize(product.getProductSize())
                    .quantity(1) // Set quantity in cart to 1
                    .brand(product.getBrand())
                    .categoryId(product.getCategoryId())
                    .feedbacks(new ArrayList<>(product.getFeedbacks()))
                    .build();
            shoppingCart.getAllItems().put(product.getProductId(), productToAdd);
        }
        shoppingCartService.setTotalPrice(userId);
        return true;
    }



    /**
     * remove the product from the shopping cart (remove one product one time).
     * @param productId
     * @param userId
     */
    public boolean removeShoesFromCart(String productId, String userId) {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        if (shoppingCart == null) {
            return false;
        }

        // Check if the product exists in the shopping cart
        if (shoppingCart.getAllItems().containsKey(productId)) {
            Product product = shoppingCart.getAllItems().get(productId);
            if (product.getQuantity() > 1) {
                // If more than one quantity, reduce the quantity by 1
                product.setQuantity(product.getQuantity() - 1);
            } else {
                // If only one quantity, remove the product from the cart
                shoppingCart.getAllItems().remove(productId);
            }
            // calculate totalPrice
            shoppingCartService.setTotalPrice(userId);
            return true;
        } else {
            // If the product is not found in the cart, return false
            return false;
        }
    }

    public boolean createNewOrder(String userId) {
        User user = systemService.findUserById(userId);
        if (!(user instanceof Buyer buyer)) {
            System.out.println("User is not a buyer or does not exist.");
            return false;
        }

        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        if (shoppingCart == null || shoppingCart.getAllItems().isEmpty()) {
            System.out.println("Shopping cart is empty.");
            return false;
        }

        // Reduce stock quantities and possibly notify sellers about low or out of stock
        for (Map.Entry<String, Product> entry : shoppingCart.getAllItems().entrySet()) {
            Product productInCart = entry.getValue();
            Product productInStock = systemService.findProductById(productInCart.getProductId());

            int newQuantity = productInStock.getQuantity() - productInCart.getQuantity();
            productInStock.setQuantity(newQuantity);
            if (newQuantity <= 0) {
                systemService.sendOutOfStockMsgToSeller(productInStock);
            }
        }

        // Generate a unique ID for the new order
        String orderId = UUID.randomUUID().toString();
        List<Product> productList = new ArrayList<>(shoppingCart.getAllItems().values());
        Order newOrder = new Order(orderId, productList, new Date(), userId, shoppingCart.getTotalPrice());

        buyer.getOrders().put(orderId, newOrder);

        return true;
    }


    /**
     * The buyer can view all his previous orders
     * @param buyerId
     * @return
     */
    public HashMap<String, Order> viewAllOrders(String buyerId) {
        User user = systemService.findUserById(buyerId);
        if (user instanceof Buyer) {
            Buyer buyer = (Buyer) user;
            // Assuming Buyer has a method or property to get their orders
            return buyer.getOrders(); // Replace getOrders() with the actual method/property name
        }
        // Return an empty list if the user is not found or not a Buyer
        return null;
    }

    /**
     * The buyer can view the specific order
     * @param orderId
     */
    public Order viewOrderByID(String orderId, String buyerId) {
        return viewAllOrders(buyerId).get(orderId);
    }

    /**
     * The buyer check out the cart
     * @param userId
     */
    public boolean cartCheckOut(String userId, String paymentId) throws Exception {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        shoppingCartService.selectPaymentMethod(userId, paymentId);
        // reset the cart
        shoppingCart = new ShoppingCart();
        return true;
    }


    public void resetCart(String userId) {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        shoppingCart = new ShoppingCart();
    }

    public void raiseComplaint(String buyerId,String sellerId, Message complaint) {
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        Seller seller = (Seller) systemService.findUserById(sellerId);
        buyer.getNotifications().add(complaint);
        seller.getNotifications().add(complaint);
    }

    /**
     * Write the feedback of the product
     * @param productId
     * @param feedback
     */
    public void writeFeedBack(String productId, String feedback) {
        Product product = systemService.findProductById(productId);
        product.getFeedbacks().add(feedback);
    }

    /**
     * View the complaints raised by me
     * @param buyerId
     */
    public List<Message> viewMyComplaints(String buyerId) {
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        return buyer.getNotifications().stream()
                .filter(message -> message.getType() == Message.MessageType.COMPLAINT)
                .collect(Collectors.toList());
    }

    public void addAddress(String buyerId, Address address){
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        buyer.getAddresses().add(address);
    }

    public void editAddress(String buyerId, String addressId, Address updatedAddress){
        Buyer buyer = (Buyer) systemService.findUserById(buyerId);
        if (buyer == null) {
            System.out.println("Buyer not found.");
            return;
        }

        List<Address> addresses = buyer.getAddresses();
        Address addressToEdit = addresses.stream()
                .filter(address -> address.getAddressID().equals(addressId))
                .findFirst()
                .orElse(null);

        if (addressToEdit == null) {
            System.out.println("Address not found.");
            return;
        }

        if (!updatedAddress.getCity().isEmpty()) {
            addressToEdit.setCity(updatedAddress.getCity());
        }
        if (!updatedAddress.getState().isEmpty()) {
            addressToEdit.setState(updatedAddress.getState());
        }
        if (!updatedAddress.getPostalCode().isEmpty()) {
            addressToEdit.setPostalCode(updatedAddress.getPostalCode());
        }
    }

    public void removeAddress(String buyerId, String addressId){
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        buyer.getAddresses().removeIf(address -> Objects.equals(address.getAddressID(), addressId));
    }

    public List<Address> viewAllAddresses(String buyerId) {
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        if (buyer != null) {
            return buyer.getAddresses();
        } else {
            return new ArrayList<>(); // Return an empty list if no buyer is found
        }
    }

    public boolean doesBuyerIdExist(String userId) {
        User user = systemService.getUsers().get(userId);
        return user instanceof Buyer;
    }

    public boolean doesAddressExist(String buyerId, String addressId) {
        User user = systemService.findUserById(buyerId);
        if (user instanceof Buyer) {
            Buyer buyer = (Buyer) user;
            return buyer.getAddresses().stream()
                    .anyMatch(address -> address.getAddressID().equals(addressId));
        }
        return false;
    }
}
