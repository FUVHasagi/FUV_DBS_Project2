package view.cashier;

import javax.swing.*;
import java.awt.*;

public class CashierMainScreen extends JFrame {
    private JButton productBrowseButton;
    private JButton newOrderButton;
    private JButton orderHistoryButton;

    public CashierMainScreen() {
        setTitle("Cashier Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Cashier Dashboard", JLabel.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        // Buttons
        productBrowseButton = new JButton("Browse Products");
        newOrderButton = new JButton("New Order");
        orderHistoryButton = new JButton("Order History");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(productBrowseButton);
        buttonPanel.add(newOrderButton);
        buttonPanel.add(orderHistoryButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    // Getters for buttons
    public JButton getProductBrowseButton() {
        return productBrowseButton;
    }

    public JButton getNewOrderButton() {
        return newOrderButton;
    }

    public JButton getOrderHistoryButton() {
        return orderHistoryButton;
    }
}
