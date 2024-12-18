package controller;

import controller.cashier.CashierMainScreenController;
import controller.customer.CustomerMainScreenController;
import controller.manager.ManagerMainScreenController;
import dataAccess.MySQL;
import dataAccess.MongoDB;
import dataAccess.Redis;
import model.Customer;
import model.User;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private MySQL mySQL;
    private MongoDB mongoDB;
    private Redis redis;

    public LoginController(LoginView loginView, MySQL mySQL, MongoDB mongoDB, Redis redis) {
        this.loginView = loginView;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;
        this.redis = redis;

        // Show the login view
        this.loginView.setVisible(true);

        // Add action listener to the login button
        loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        String username = loginView.getUsernameField().getText();
        String password = String.valueOf(loginView.getPasswordField().getPassword());
        User user = mySQL.authenticateUser(username, password);

        if (user != null) {
            loginView.dispose(); // Close the login screen

            String role = user.getRole(); // Get the user's role

            switch (role) {
                case "customer":
                    // Fetch associated Customer ID from MongoDB
                    String customerId = user.getCustomer().getId();
                    Customer customer = mongoDB.getCustomerById(customerId);
                    if (customer != null) {
                        user.setCustomer(customer);
                        new CustomerMainScreenController(user, customerId, mySQL, mongoDB, redis);
                    } else {
                        JOptionPane.showMessageDialog(loginView, "Customer not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "cashier":
                    new CashierMainScreenController(user, mySQL, mongoDB, redis);
                    break;
                case "manager":
                    new ManagerMainScreenController(user, mySQL, mongoDB, redis);
                    break;
                default:
                    JOptionPane.showMessageDialog(loginView, "Unknown user role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loginView.displayErrorMessage("Invalid username or password");
        }
    }
}
