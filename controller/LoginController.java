package controller;

import controller.cashier.CashierMainScreenController;
import controller.customer.CustomerMainScreenController;
import controller.manager.ManagerMainScreenController;
import dataAccess.MySQL;
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

    public LoginController(LoginView loginView, MySQL mySQL) {
        this.loginView = loginView;
        this.mySQL = mySQL;

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

            switch (role) {  // Using switch for clarity and potential expansion
                case "customer":
                    new CustomerMainScreenController(user.getCustomer()); // Open CustomerMainScreen
                    break;
                case "cashier":
                    new CashierMainScreenController(new CashierMainScreen()); // Open CashierMainScreen
                    break;
                case "manager":
                    new ManagerMainScreenController(new ManagerMainScreen()); // Open ManagerMainScreen
                    break;
                default:
                    // Handle unknown roles (e.g., log error or show a message)
                    JOptionPane.showMessageDialog(loginView, "Unknown user role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            loginView.displayErrorMessage("Invalid username or password");
        }
    }
}
