package controller.cashier;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.Order;
import model.OrderLine;
import model.Product;
import model.User;
import view.cashier.CashierOrderView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CashierOrderController implements ActionListener {
    private CashierOrderView view;
    private User cashier;
    private MySQL mySQL;
    private MongoDB mongoDB;
    private List<OrderLine> orderLines;
    private String customerId;

    public CashierOrderController(User cashier, MySQL mySQL, MongoDB mongoDB) {
        this.cashier = cashier;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;
        this.orderLines = new ArrayList<>();

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
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter Quantity:"));
            OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
            orderLines.add(orderLine);
            updateOrderLinesTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        orderLines.clear();
        updateOrderLinesTable();
    }

    private void handleFinishAndPay() {
        if (orderLines.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Order is empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order order = new Order();
        order.setSourceType("cashier");
        order.setSourceID(String.valueOf(cashier.getId()));
        order.setCustomerID(customerId);
        order.setLines(orderLines);
        order.setTotalCost(orderLines.stream().mapToDouble(OrderLine::getCost).sum());

        mongoDB.saveOrder(order);

        JOptionPane.showMessageDialog(view, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        handleClearOrder();
    }

    private void handleChangeProduct() {
        int selectedProductId = view.getOrderLinePanel().getSelectedProductID();
        if (selectedProductId != -1) {
            Product product = view.getProductInfoPanel().getProduct();
            orderLines.removeIf(orderLine -> orderLine.getProductID() == selectedProductId);
            try {
                int quantity = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter Quantity:"));
                OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
                orderLines.add(orderLine);
                updateOrderLinesTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid Quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleAddCustomer() {
        customerId = JOptionPane.showInputDialog(view, "Enter Customer ID:");
    }

    private void updateOrderLinesTable() {
        view.getOrderLinePanel().setTableData(orderLines);
    }
}
