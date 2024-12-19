package controller.cashier;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.*;
import view.cashier.CashierOrderView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CashierOrderController implements ActionListener {
    private CashierOrderView view;
    private User cashier;
    private MySQL mySQL;
    private MongoDB mongoDB;
    private List<OrderLine> orderLines; // should be changed to Dict for quantity <>
    private String customerId;
    private Map<Integer, Integer> productQuantityMap; // Map for product ID and quantity


    public CashierOrderController(User cashier, MySQL mySQL, MongoDB mongoDB) {
        this.cashier = cashier;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;
        this.orderLines = new ArrayList<>();
        this.productQuantityMap = new HashMap<>();


        view = new CashierOrderView();
        initializeListeners();
        view.setVisible(true);
    }

    private void initializeListeners() {
        view.getAddProductButton().addActionListener(this);
        view.getDeleteProductButton().addActionListener(this);
        view.getLoadProductButton().addActionListener(this);
        view.getClearOrderButton().addActionListener(this);
        view.getFinishAndPayButton().addActionListener(this);
        view.getChangeProductButton().addActionListener(this);
        view.getAddCustomerButton().addActionListener(this);
    }

    // Helper method to generate a unique order ID
    private String generateOrderID() {
        return "ORD-" + System.currentTimeMillis(); // Example: Unique ID based on timestamp
    }

    // Helper method to get the current date in a specific format
    private String generateOrderDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddProductButton()) {
            handleAddProduct();
        } else if (e.getSource() == view.getDeleteProductButton()) {
            handleDeleteProduct();
        } else if (e.getSource() == view.getLoadProductButton()) {
            handleLoadProduct();
        } else if (e.getSource() == view.getClearOrderButton()) {
            handleClearOrder();
        } else if (e.getSource() == view.getFinishAndPayButton()) {
            handleFinishAndPay();
        } else if (e.getSource() == view.getChangeProductButton()) {
            handleChangeProduct();
        } else if (e.getSource() == view.getAddCustomerButton()) {
            handleAddCustomer();
        }
    }

    private void handleAddProduct() {
        try {
            Product product = view.getProductInfoPanel().getProduct();
            if (product == null) {
                JOptionPane.showMessageDialog(view, "No product loaded. Please load a product first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter Quantity:"));
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(view, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!mySQL.isStockSufficient(product.getId(), quantity)) {
                JOptionPane.showMessageDialog(view,
                        "Insufficient stock for product: " + product.getName(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update quantity in the Map
            productQuantityMap.merge(product.getId(), quantity, Integer::sum);

            updateOrderLinesTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteProduct() {
        int selectedProductId = view.getOrderLinePanel().getSelectedProductID();
        orderLines.removeIf(orderLine -> orderLine.getProductID() == selectedProductId);
        updateOrderLinesTable();
    }

    private void handleLoadProduct() {
        try {
            int productId = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter Product ID:"));
            Product product = mySQL.getProductById(productId);
            if (product != null) {
                view.getProductInfoPanel().setProduct(product);
            } else {
                JOptionPane.showMessageDialog(view, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Invalid Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleClearOrder() {
        productQuantityMap.clear();
        updateOrderLinesTable();
    }

    private void handleFinishAndPay() {
        if (productQuantityMap.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Order is empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verify stock availability
        for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
            if (!mySQL.isStockSufficient(entry.getKey(), entry.getValue())) {
                JOptionPane.showMessageDialog(view,
                        "Insufficient stock for product ID: " + entry.getKey(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Create a new order
        Order order = new Order();
        order.setOrderID(generateOrderID()); // Generate a unique order ID
        order.setSourceType("cashier");
        order.setSourceID(String.valueOf(cashier.getId()));
        order.setCustomerID(customerId != null ? customerId : ""); // Handle null customer ID
        order.setOrderDate(generateOrderDate()); // Generate the current timestamp
        order.setLines(new ArrayList<>()); // Ensure lines is initialized

        // Populate order lines and reduce stock
        for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
            Product product = mySQL.getProductById(entry.getKey());
            if (product != null) {
                OrderLine orderLine = OrderLine.createFromProduct(product, entry.getValue());
                order.getLines().add(orderLine); // Add to the order's lines
                mySQL.reduceProductStock(product.getId(), entry.getValue());
            }
        }

        // Calculate total cost
        double totalCost = order.getLines().stream()
                .mapToDouble(OrderLine::getCost)
                .sum();
        order.setTotalCost(totalCost);

        // Save the order in MongoDB
        mongoDB.saveOrder(order);

        // Show success message and clear the order
        JOptionPane.showMessageDialog(view, "Order placed successfully! Order ID: " + order.getOrderID(), "Success", JOptionPane.INFORMATION_MESSAGE);
        handleClearOrder();
    }




    private void handleChangeProduct() {
        int selectedProductId = view.getOrderLinePanel().getSelectedProductID();
        if (selectedProductId != -1) {
            try {
                // Prompt for new Product ID with default value as the current Product ID
                String newProductIdInput = JOptionPane.showInputDialog(view,
                        "Enter new Product ID:",
                        String.valueOf(selectedProductId)
                );
                if (newProductIdInput == null || newProductIdInput.trim().isEmpty()) {
                    return; // User cancelled or entered no value
                }

                int newProductId = Integer.parseInt(newProductIdInput.trim());
                Product newProduct = mySQL.getProductById(newProductId);

                if (newProduct == null) {
                    JOptionPane.showMessageDialog(view, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Prompt for new quantity with default value as the current quantity
                int currentQuantity = productQuantityMap.getOrDefault(selectedProductId, 0);
                String newQuantityInput = JOptionPane.showInputDialog(view,
                        "Enter new Quantity:",
                        String.valueOf(currentQuantity)
                );
                if (newQuantityInput == null || newQuantityInput.trim().isEmpty()) {
                    return; // User cancelled or entered no value
                }

                int newQuantity = Integer.parseInt(newQuantityInput.trim());
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(view, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!mySQL.isStockSufficient(newProductId, newQuantity)) {
                    JOptionPane.showMessageDialog(view,
                            "Insufficient stock for product: " + newProduct.getName(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the productQuantityMap
                productQuantityMap.remove(selectedProductId); // Remove old product ID
                productQuantityMap.put(newProductId, newQuantity); // Add new product ID with new quantity

                updateOrderLinesTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "No product selected. Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleAddCustomer() {
        customerId = JOptionPane.showInputDialog(view, "Enter Customer ID:");

    }

    private void updateOrderLinesTable() {
        // Clear the current orderLines list and rebuild it from the productQuantityMap
        orderLines.clear();
        for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = mySQL.getProductById(productId);
            if (product != null) {
                OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
                orderLines.add(orderLine);
            }
        }

        // Update the table with the new orderLines list
        view.getOrderLinePanel().setTableData(orderLines);
    }

}
