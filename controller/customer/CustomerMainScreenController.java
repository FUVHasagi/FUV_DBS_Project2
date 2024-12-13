package controller.customer;

import view.customer.CustomerMainScreen;
import controller.customer.CustomerProductPanelController;
import controller.customer.CartController;
import controller.customer.OrderHistoryController;

import model.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMainScreenController implements ActionListener {
    private CustomerMainScreen view;
    private Customer customer;

    public CustomerMainScreenController(Customer customer) {
        this.customer = customer;
        this.view = new CustomerMainScreen();
        this.view.addActionListener(this);
        this.view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getProductViewButton()) {
            // Open CustomerProductPanel and pass the customer
            new CustomerProductPanelController(customer);
        } else if (e.getSource() == view.getCartButton()) {
            // Open CartView and pass the customer
            new CartController(customer);
        } else if (e.getSource() == view.getOrderHistoryButton()) {
            // Open OrderHistory and pass the customer
            new OrderHistoryController(customer);
        }
    }
}
