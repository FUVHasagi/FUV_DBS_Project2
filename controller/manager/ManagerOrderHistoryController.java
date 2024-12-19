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

        // Initialize view
        this.view = new ManagerOrderHistoryView();

        // Refresh cache with recent orders on initialization
        try {
            List<Order> recentOrders = mongoDB.getOrdersBySource("all", null, false);
            redis.cacheOrders("recent_orders", recentOrders);
        } catch (Exception e) {
            System.err.println("Failed to refresh recent orders cache: " + e.getMessage());
        }

        // Load recent orders into the view
        loadRecentOrders();

        // Add action listeners
        view.getShowAllButton().addActionListener(this);
        view.getInspectOrderButton().addActionListener(this);

        // Display the view
        this.view.setVisible(true);
    }


    private void loadRecentOrders() {
        try {
            List<Order> recentOrders = redis.getCachedOrders("recent_orders");

            if (recentOrders.isEmpty()) {
                recentOrders = mongoDB.getOrdersBySource("all", null, false);
                redis.cacheOrders("recent_orders", recentOrders);
            }

            if (recentOrders.isEmpty()) {
                JOptionPane.showMessageDialog(view, "No recent orders found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.getBrowseOrderHistory().setTableData(recentOrders);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load recent orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void loadAllOrders() {
        try {
            List<Order> allOrders = mongoDB.getOrdersBySource("all", null, false);

            if (allOrders.isEmpty()) {
                JOptionPane.showMessageDialog(view, "No orders found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.getBrowseOrderHistory().setTableData(allOrders);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Failed to load all orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Order> fetchOrdersFromRedis(String cacheKey) {
        return redis.getCachedOrders(cacheKey);
    }

    private void cacheOrdersToRedis(List<Order> orders, String cacheKey) {
        redis.cacheOrders(cacheKey, orders);
    }

    private void inspectOrder() {
        String selectedOrderId = view.getBrowseOrderHistory().getSelectedOrderID();
        if (selectedOrderId != null) {
            new ManagerOrderDetailController(selectedOrderId, mongoDB); // Replace with actual detail controller
        } else {
            JOptionPane.showMessageDialog(view, "Please select an order to inspect.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getShowAllButton()) {
            loadAllOrders();
        } else if (e.getSource() == view.getInspectOrderButton()) {
            inspectOrder();
        }
    }
}
