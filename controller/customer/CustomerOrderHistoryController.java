package controller.customer;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.Customer;
import model.Order;
import view.customer.CustomerOrderHistory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerOrderHistoryController implements ActionListener {
    private CustomerOrderHistory view;
    private Customer customer;
    private MongoDB mongoDB;
    private MySQL mySQL;

    public CustomerOrderHistoryController(Customer customer, MongoDB mongoDB, MySQL mySQL) {
        this.customer = customer;
        this.mongoDB = mongoDB;
        this.view = new CustomerOrderHistory();
        this.mySQL = mySQL;

        // Populate order history
        loadOrderHistory();

        // Add action listeners
        view.getInspectOrderButton().addActionListener(this);

        // Display the view
        view.setVisible(true);
    }

    private void loadOrderHistory() {
        try {
            // Get orders placed by the customer
            List<Order> customerOrders = mongoDB.getOrdersBySource("customer", customer.getId(), false);

            // Get orders placed by cashiers for the customer
            List<Order> cashierOrdersForCustomer = mongoDB.getOrdersBySource("cashier", customer.getId(), true);

            customerOrders.addAll(cashierOrdersForCustomer);

            if (customerOrders.isEmpty()) {
                JOptionPane.showMessageDialog(view, "No orders found for this customer.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.getBrowseOrderHistory().setTableData(customerOrders);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load order history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getInspectOrderButton()) {
            String selectedOrderID = view.getBrowseOrderHistory().getSelectedOrderID();
            if (selectedOrderID != null) {
                // Retrieve the selected order from MongoDB
                Order selectedOrder = mongoDB.getOrdersBySource("all", null, false)
                        .stream()
                        .filter(order -> order.getOrderID().equals(selectedOrderID))
                        .findFirst()
                        .orElse(null);

                if (selectedOrder != null) {
                    // Open the CustomerOrderDetailController
                    new CustomerOrderDetailController(selectedOrder, customer, mongoDB, mySQL);
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to retrieve the selected order.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Please select an order to inspect.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
