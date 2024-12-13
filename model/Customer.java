package model;

public class Customer {
    private String fullName;
    private String id;
    private Cart cart;

    // Constructor
    public Customer(String fullName, String id) {
        this.fullName = fullName;
        this.id = id;
        this.cart = new Cart(); // Initialize an empty cart
    }

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter for cart
    public Cart getCart() {
        return cart;
    }

    // Methods to interact with the cart
    public void addItemToCart(OrderLine orderLine) {
        if (orderLine != null) {
            cart.addItem(orderLine);
        }
    }

    public void removeItemFromCart(OrderLine orderLine) {
        if (orderLine != null) {
            cart.removeItem(orderLine);
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public double calculateTotal() {
        return cart.calculateTotal();
    }
}