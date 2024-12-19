package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Customer {
    private String fullName;
    private String id;
    private Cart cart;
    private HashSet<Integer> boughtProducts = new HashSet<>();

    // Constructor
    public Customer(String fullName, String id) {
        this.fullName = fullName;
        this.id = id;
        this.cart = new Cart(); // Initialize an empty cart
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    // Cart Operations
    public void addItemToCart(OrderLine orderLine) {
        if (orderLine != null) {
            cart.addItem(orderLine);
        }
    }

    public void removeItemFromCart(OrderLine orderLine) {
        if (orderLine != null) {
            cart.removeItem(orderLine.getProductID());
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public double calculateTotal() {
        return cart.calculateTotal();
    }

    // Method to create an Order
    public Order createOrder() {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create order.");
        }

        Order order = new Order();
        order.setOrderID(generateOrderID());
        order.setCustomerID(this.id);
        order.setCustomerName(this.fullName);
        order.setOrderDate(generateOrderDate());
        order.setLines(new ArrayList<>(cart.getItems().values()));
        order.setTotalCost(cart.calculateTotal());
        return order;
    }

    // Helper method to generate a unique order ID
    private String generateOrderID() {
        return "ORD-" + System.currentTimeMillis(); // Example: Unique ID based on timestamp
    }

    // Helper method to get the current date in a specific format
    private String generateOrderDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // Method to set the bought products
    public void setBoughtProducts(HashSet<Integer> boughtProducts) {
        if (boughtProducts != null) {
            this.boughtProducts = boughtProducts;
        } else {
            this.boughtProducts = new HashSet<>();
        }
    }

    // Method to get the bought products (optional, if needed elsewhere)
    public HashSet<Integer> getBoughtProducts() {
        return this.boughtProducts;
    }

    // Method to check if a product has been bought by the customer
    public boolean hasBoughtProduct(int productId) {
        return boughtProducts.contains(productId);
    }

    // Method to add a product to the bought products
    public void addBoughtProduct(int productId) {
        boughtProducts.add(productId);
    }
}
