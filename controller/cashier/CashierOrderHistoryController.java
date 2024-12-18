package controller.cashier;

import dataAccess.MongoDB;
import model.Order;
import model.User;
import view.cashier.CashierOrderHistoryView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierOrderHistoryController implements ActionListener {
    private CashierOrderHistoryView view;
    private User cashier;
    private MongoDB mongoDB;

    public CashierOrderHistoryController(User cashier, MongoDB mongoDB) {
        this.cashier = cashier;
        this.mongoDB = mongoDB;

        // Initialize view
        this.view = new CashierOrderHistoryView();

        // Populate order history
        loadOrderHistory();

        // Add action listener
        this.view.getInspectOrderButton().addActionListener(this);

        // Display the view
        this.view.setVisible(true);
    }

    private void loadOrderHistory() {
        try {
            var orders = mongoDB.getOrdersBySource("cashier", String.valueOf(cashier.getId()));
            view.getBrowseOrderHistory().setTableData(orders);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load order history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getInspectOrderButton()) {
            handleInspectOrder();
        }
    }

    private void handleInspectOrder() {
        var selectedOrderId = view.getBrowseOrderHistory().getSelectedOrderID();
        if (selectedOrderId != null) {
            // Call the Order Detail View and Controller (to be implemented later)
            new CashierOrderDetailController(selectedOrderId, mongoDB, cashier);
        } else {
            JOptionPane.showMessageDialog(view, "Please select an order to inspect.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
