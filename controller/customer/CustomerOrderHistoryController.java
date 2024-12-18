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
        List<Order> orders = mongoDB.getOrdersBySource("customer", customer.getId());
        view.getBrowseOrderHistory().setTableData(orders);
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
