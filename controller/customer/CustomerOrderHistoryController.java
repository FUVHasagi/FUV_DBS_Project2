package controller.customer;

import dataAccess.MongoDB;
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

    public CustomerOrderHistoryController(Customer customer, MongoDB mongoDB) {
        this.customer = customer;
        this.mongoDB = mongoDB;
        this.view = new CustomerOrderHistory();

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
                // Call CustomerOrderDetailController to handle detailed view (placeholder for now)
                JOptionPane.showMessageDialog(view, "Inspecting Order: " + selectedOrderID, "Order Detail", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(view, "Please select an order to inspect.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
