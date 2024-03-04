package com.coen6312.ecommerce.service;

import com.coen6312.ecommerce.entity.Buyer;
import com.coen6312.ecommerce.entity.PaymentMethod;
import com.coen6312.ecommerce.entity.Product;
import com.coen6312.ecommerce.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    private final EcommerceSystemService systemService;

    public ShoppingCartService(EcommerceSystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * Find shopping cart by user id
     * @param userId
     * @return
     */
    public ShoppingCart findShoppingCartByUserId(String userId){
        try {
            Buyer buyer = (Buyer) systemService.findUserById(userId);
            if (buyer != null) {
                return buyer.getShoppingCart(); // Assuming Buyer has a getShoppingCart() method
            }
        } catch (ClassCastException e) {
            System.err.println("The user with ID " + userId + " is not a Buyer.");
        }
        return null;
    }

    /**
     * Show the specific product quantity in the cart.
     * @param userId
     */
    public int showQuantityByID(String productId, String userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        Product product = shoppingCart.getAllItems().get(productId);
        if (product != null) {
            return product.getQuantity();
        } else {
            return 0;
        }
    }

    /**
     * Select the payment method
     * @param paymentId
     */
    public void selectPaymentMethod(String userId, String paymentId) throws Exception {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        PaymentMethod paymentMethod = PaymentMethod.getPaymentMethodById(paymentId);
        if (paymentMethod != null) {
            shoppingCart.setPaymentMethod(paymentMethod.getMethodName());
        } else {
            throw new Exception("Payment method not found for ID: " + paymentId);
        }
    }

    public void setTotalPrice(String userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        shoppingCart.setTotalPrice(
                shoppingCart.getAllItems().values().stream()
                        .mapToDouble(product -> product.getUnitPrice() * product.getQuantity())
                        .sum()
        );
    }
}
