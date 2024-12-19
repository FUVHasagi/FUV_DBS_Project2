package controller.manager;

import dataAccess.MongoDB;
import dataAccess.Redis;
import model.Order;
import view.manager.ManagerOrderHistoryView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagerOrderHistoryController implements ActionListener {
    private ManagerOrderHistoryView view;
    private MongoDB mongoDB;
    private Redis redis;

    public ManagerOrderHistoryController(MongoDB mongoDB, Redis redis) {
        this.mongoDB = mongoDB;
        this.redis = redis;
        this.view = new ManagerOrderHistoryView();

        loadRecentOrders();
        view.getShowAllButton().addActionListener(this);
        view.getInspectOrderButton().addActionListener(this);
        view.setVisible(true);
    }

    private void loadRecentOrders() {
        try {
            // Load recent orders from Redis cache
            List<Order> recentOrders = redis.getCachedOrders("recent_orders");
            if (recentOrders.isEmpty()) {
                // If no cached recent orders, fetch from MongoDB and cache them
                recentOrders = mongoDB.getOrdersBySource("recent", null, false);
                redis.cacheOrders("recent_orders", recentOrders);
            }
            // Update the view with the recent orders
            view.getBrowseOrderHistory().setTableData(recentOrders);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load recent orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllOrders() {
        try {
            // Load all orders directly from MongoDB
            List<Order> allOrders = mongoDB.getOrdersBySource("all", null, false);
            // Update the view with all orders
            view.getBrowseOrderHistory().setTableData(allOrders);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load all orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getShowAllButton()) {
            loadAllOrders(); // Load all orders when "Show All" is clicked
        } else if (e.getSource() == view.getInspectOrderButton()) {
            inspectOrder(); // Inspect selected order
        }
    }

    private void inspectOrder() {
        String selectedOrderId = view.getBrowseOrderHistory().getSelectedOrderID();
        if (selectedOrderId != null) {
            JOptionPane.showMessageDialog(view, "Order Details for: " + selectedOrderId, "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Please select an order.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
