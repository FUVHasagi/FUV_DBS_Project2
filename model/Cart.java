package model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Integer, OrderLine> items = new HashMap<>();

    // Add an item to the cart
    public void addItem(OrderLine orderLine) {
        if (orderLine != null) {
            int productID = orderLine.getProductID();
            if (items.containsKey(productID)) {
                // If product already in cart, update quantity and cost
                OrderLine existingOrderLine = items.get(productID);
                existingOrderLine.setQuantity(existingOrderLine.getQuantity() + orderLine.getQuantity());
                existingOrderLine.setCost();
            } else {
                // Add new product to cart
                orderLine.setCost();
                items.put(productID, orderLine);
            }
        }
    }

    // Remove an item from the cart
    public void removeItem(OrderLine orderLine) {
        if (orderLine != null) {
            items.remove(orderLine.getProductID());
        }
    }

    // Clear all items from the cart
    public void clear() {
        items.clear();
    }

    // Get the total price of all items in the cart
    public double calculateTotal() {
        double total = 0.0;
        for (OrderLine orderLine : items.values()) {
            total += orderLine.getCost();
        }
        return total;
    }

    // Getter for items (optional, if needed for further operations)
    public Map<Integer, OrderLine> getItems() {
        return items;
    }

    public void addProduct(Product selectedProduct, int i) {
        OrderLine temp = OrderLine.createFromProduct(selectedProduct, i);
        addItem(temp);
    }
}
