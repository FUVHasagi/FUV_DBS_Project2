package controller.cashier;

import dataAccess.MongoDB;
import model.Order;
import model.OrderLine;
import model.User;
import view.cashier.CashierOrderDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CashierOrderDetailController implements ActionListener {
    private CashierOrderDetailView view;
    private Order order;
    private MongoDB mongoDB;
    private User cashier;

    public CashierOrderDetailController(String orderId, MongoDB mongoDB, User cashier) {
        this.mongoDB = mongoDB;
        this.cashier = cashier;

        // Fetch the order details using the provided orderId
        this.order = fetchOrderDetails(orderId);

        // Initialize the view
        this.view = new CashierOrderDetailView();

        // Populate the order lines
        loadOrderLines();

        // Add action listener to the close button
        view.getCloseButton().addActionListener(this);

        // Display the view
        this.view.setVisible(true);
    }

    private Order fetchOrderDetails(String orderId) {
        try {
            List<Order> orders = mongoDB.getOrdersBySource("cashier", String.valueOf(cashier.getId()), true);
            for (Order ord : orders) {
                if (ord.getOrderID().equals(orderId)) {
                    return ord;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch order details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void loadOrderLines() {
        if (order != null) {
            List<OrderLine> orderLines = order.getLines();
            view.getBrowseOrderLine().setTableData(orderLines);
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
