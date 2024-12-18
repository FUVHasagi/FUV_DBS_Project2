package controller.customer;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;
import view.customer.CustomerOrderDetailView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerOrderDetailController implements ActionListener {
    private CustomerOrderDetailView view;
    private Order order;
    private Customer customer;
    private MongoDB mongoDB;
    private MySQL mySQL;

    public CustomerOrderDetailController(Order order, Customer customer, MongoDB mongoDB, MySQL mySQL) {
        this.order = order;
        this.customer = customer;
        this.mongoDB = mongoDB;
        this.mySQL = mySQL;
        this.view = new CustomerOrderDetailView();

        // Populate order line data
        loadOrderLines();

        // Add action listeners
        view.getReviewProductButton().addActionListener(this);
        view.getBuyAgainButton().addActionListener(this);

        // Display the view
        view.setVisible(true);
    }

    private void loadOrderLines() {
        List<OrderLine> orderLines = order.getLines();
        view.getBrowseOrderLine().setTableData(orderLines);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBuyAgainButton()) {
            handleBuyAgain();
        } else if (e.getSource() == view.getReviewProductButton()) {
            handleReviewProduct();
        }
    }

    private void handleBuyAgain() {
        int selectedProductID = view.getBrowseOrderLine().getSelectedProductID();
        if (selectedProductID == -1) {
            JOptionPane.showMessageDialog(view, "Please select a product to buy again.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = mySQL.getProductById(selectedProductID);
        if (product == null) {
            JOptionPane.showMessageDialog(view, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, product.getStockQuantity(), 1);
        JSpinner spinner = new JSpinner(model);
        int result = JOptionPane.showConfirmDialog(view, spinner, "Enter Quantity", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int quantity = (int) spinner.getValue();
            try {
                OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
                customer.addItemToCart(orderLine);
                JOptionPane.showMessageDialog(view, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view, "Error Message", "Error Title", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void handleReviewProduct() {
        int selectedProductID = view.getBrowseOrderLine().getSelectedProductID();
        if (selectedProductID == -1) {
            JOptionPane.showMessageDialog(view, "Please select a product to review.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = mySQL.getProductById(selectedProductID);
        if (product == null) {
            JOptionPane.showMessageDialog(view, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open the product review view (CustomerProductInfoView)
        new CustomerProductInfoController(product, customer, mongoDB);
    }
}
