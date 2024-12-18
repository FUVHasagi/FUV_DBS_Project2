package controller.customer;

import dataAccess.MongoDB;
import model.Cart;
import model.Customer;
import model.Order;
import model.OrderLine;
import view.customer.CartView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CartController implements ActionListener {
    private CartView view;
    private Customer customer;
    private MongoDB mongoDB;

    public CartController(Customer customer, MongoDB mongoDB) {
        this.customer = customer;
        this.mongoDB = mongoDB;

        this.view = new CartView();
        populateCartTable();
        addListeners();
        this.view.setVisible(true);
    }

    private void populateCartTable() {
        // Populate the cart table using data from the customer's cart
        Map<Integer, OrderLine> items = customer.getCart().getItems();
        Object[][] data = items.values().stream()
                .map(item -> new Object[]{
                        item.getProductID(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getCost()
                }).toArray(Object[][]::new);

        DefaultTableModel tableModel = (DefaultTableModel) view.getBrowseOrderLine().getOrderLineTable().getModel();
        tableModel.setRowCount(0); // Clear existing rows

        for (Object[] row : data) {
            tableModel.addRow(row);
        }

        updateTotalCost();
    }

    private void addListeners() {
        view.getDeleteFromCartButton().addActionListener(this);
        view.getChangeQuantityButton().addActionListener(this);
        view.getFinishAndPayButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getDeleteFromCartButton()) {
            deleteSelectedItem();
        } else if (e.getSource() == view.getChangeQuantityButton()) {
            changeItemQuantity();
        } else if (e.getSource() == view.getFinishAndPayButton()) {
            finishAndPay();
        }
    }

    private void deleteSelectedItem() {
        JTable table = view.getBrowseOrderLine().getOrderLineTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) table.getValueAt(selectedRow, 0);
            customer.getCart().removeItem(productId);
            populateCartTable();
            JOptionPane.showMessageDialog(view, "Item removed from cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Please select an item to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void changeItemQuantity() {
        JTable table = view.getBrowseOrderLine().getOrderLineTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) table.getValueAt(selectedRow, 0);
            String newQuantityStr = JOptionPane.showInputDialog(view, "Enter new quantity:");
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(view, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Map<Integer, OrderLine> items = customer.getCart().getItems();
                if (items.containsKey(productId)) {
                    OrderLine item = items.get(productId);
                    item.setQuantity(newQuantity);
                    item.setCost();
                    populateCartTable();
                    JOptionPane.showMessageDialog(view, "Quantity updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Please select an item to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void finishAndPay() {
        if (customer.getCart().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Cart is empty. Add items before checkout.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view, "Do you want to proceed to checkout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Order order = customer.createOrder(); // Create the order
                mongoDB.saveOrder(order);            // Save the order in MongoDB
                customer.clearCart();                // Clear the cart after successful order
                populateCartTable();                 // Refresh the cart table
                JOptionPane.showMessageDialog(view, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void updateTotalCost() {
        double totalCost = customer.getCart().calculateTotal();
        view.updateTotalCost(totalCost);
    }
}
