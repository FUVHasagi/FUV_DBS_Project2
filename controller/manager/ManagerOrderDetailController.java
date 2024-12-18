package controller.manager;

import dataAccess.MongoDB;
import model.Order;
import model.OrderLine;
import view.manager.ManagerOrderDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagerOrderDetailController implements ActionListener {
    private ManagerOrderDetailView view;
    private Order order;
    private MongoDB mongoDB;

    public ManagerOrderDetailController(String orderId, MongoDB mongoDB) {
        this.mongoDB = mongoDB;

        // Fetch the order details using the provided orderId
        this.order = fetchOrderDetails(orderId);

        // Initialize the view
        this.view = new ManagerOrderDetailView();

        // Populate the order lines and total cost
        loadOrderDetails();

        // Add action listener to the close button
        view.getCloseButton().addActionListener(this);

        // Display the view
        this.view.setVisible(true);
    }

    private Order fetchOrderDetails(String orderId) {
        try {
            List<Order> allOrders = mongoDB.getOrdersBySource("all", null);
            for (Order ord : allOrders) {
                if (ord.getOrderID().equals(orderId)) {
                    return ord;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch order details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void loadOrderDetails() {
        if (order != null) {
            List<OrderLine> orderLines = order.getLines();
            view.getBrowseOrderLine().setTableData(orderLines);
            view.setTotalCost(order.getTotalCost());
        } else {
            JOptionPane.showMessageDialog(view, "Order details not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getCloseButton()) {
            view.dispose();
        }
    }
}
