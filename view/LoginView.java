package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;


    public LoginView() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the app when Login window closes
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);


        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;  add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(passwordField, gbc);


        gbc.gridx = 1; gbc.gridy = 2; add(loginButton, gbc);

        setLocationRelativeTo(null); // Center on screen

        setVisible(true);
    }



    public JButton getLoginButton() {
        return loginButton;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}