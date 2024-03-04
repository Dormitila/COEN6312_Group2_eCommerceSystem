package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // Handle case where shopping cart is not found for the user
            return false;
        }
        // add products to all items
        if (shoppingCart.getAllItems().containsKey(product.getProductId())) {
            // If the product exists, increment its quantity by 1
            Product existingProduct = shoppingCart.getAllItems().get(product.getProductId());
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
        } else {
            // If the product does not exist, set its quantity to 1 and add it to the cart
            product.setQuantity(1);
            shoppingCart.getAllItems().put(product.getProductId(), product);
        }
        // calculate totalPrice
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
     * @param cartId
     */
    public boolean cartCheckOut(String userId, String cartId, String paymentId) throws Exception {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUserId(userId);
        shoppingCartService.selectPaymentMethod(userId, paymentId);
        // reset the cart
        shoppingCart = new ShoppingCart();
        return true;
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
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        List<Address> addresses = buyer.getAddresses();
        for (Address address : addresses) {
            if (addressId == address.getAddressID()) {
                address.setCity(updatedAddress.getCity());
                address.setState(updatedAddress.getState());
                address.setPostalCode(updatedAddress.getPostalCode());
                break;
            }
        }
    }

    public void removeAddress(String buyerId, String addressId){
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        buyer.getAddresses().removeIf(address -> address.getAddressID() == addressId);
    }

    public List<Address> addresses(String buyerId){
        Buyer buyer = (Buyer)systemService.findUserById(buyerId);
        return buyer.getAddresses();
    }
}
