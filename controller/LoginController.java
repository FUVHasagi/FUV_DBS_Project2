package controller;

import controller.cashier.CashierMainScreenController;
import controller.customer.CustomerMainScreenController;
import controller.manager.ManagerMainScreenController;
import dataAccess.MySQL;
import dataAccess.MongoDB;
import model.Customer;
import model.User;
import view.LoginView;
import view.cashier.CashierMainScreen;
import view.customer.CustomerMainScreen;
import view.manager.ManagerMainScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private MySQL mySQL;
    private MongoDB mongoDB;

    public LoginController(LoginView loginView, MySQL mySQL, MongoDB mongoDB) {
        this.loginView = loginView;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;

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
                    // Fetch associated Customer from MongoDB
                    Customer customer = mongoDB.getCustomerById(user.getCustomer().getId());
                    if (customer != null) {
                        user.setCustomer(customer); // Associate the retrieved customer with the user
                        new CustomerMainScreenController(customer); // Open CustomerMainScreen
                    } else {
                        JOptionPane.showMessageDialog(loginView, "Customer not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "cashier":
                    new CashierMainScreenController(new CashierMainScreen()); // Open CashierMainScreen
                    break;
                case "manager":
                    new ManagerMainScreenController(new ManagerMainScreen()); // Open ManagerMainScreen
                    break;
                default:
                    JOptionPane.showMessageDialog(loginView, "Unknown user role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            loginView.displayErrorMessage("Invalid username or password");
        }
    }
}
