package controller;

import dataAccess.MySQL;
import model.User;
import view.LoginView;

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

    private void authenticateUser(String username, String password) {
        String username = loginView.getUsernameField().getText();
        String password = String.valueOf(loginView.getPasswordField().getPassword());

        User user = mySQL.authenticateUser(username, password);

        if (user != null) {
            loginView.dispose(); // Close the login screen
            openMainScreen(user); // Open main screen with user's role
        } else {
            loginView.displayErrorMessage("Invalid username or password");
        }
    }
}
