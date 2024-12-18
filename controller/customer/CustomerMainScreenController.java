package controller.customer;

import dataAccess.MySQL;
import dataAccess.MongoDB;
import dataAccess.Redis;
import model.Customer;
import model.User;
import view.customer.CustomerMainScreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMainScreenController implements ActionListener {
    private CustomerMainScreen view;
    private User user;
    private String customerId;
    private MySQL mySQL;
    private MongoDB mongoDB;
    private Redis redis;

    public CustomerMainScreenController(User user, String customerId, MySQL mySQL, MongoDB mongoDB, Redis redis) {
        this.user = user;
        this.customerId = customerId;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;
        this.redis = redis;

        this.view = new CustomerMainScreen();
        this.view.setVisible(true);
        addListeners();
    }

    private void addListeners() {
        view.getProductViewButton().addActionListener(this);
        view.getCartButton().addActionListener(this);
        view.getOrderHistoryButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getProductViewButton()) {
            // Open product view
            new CustomerProductPanelController(user.getCustomer(), mySQL, mongoDB);
        } else if (e.getSource() == view.getCartButton()) {
            // Open cart view
            new CartController(user.getCustomer(), mongoDB);
        } else if (e.getSource() == view.getOrderHistoryButton()) {
            // Fetch and open order history
            new OrderHistoryController(user.getCustomer(), mongoDB);
        }
    }
}
