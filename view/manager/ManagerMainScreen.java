package view.manager;

import javax.swing.*;
import java.awt.*;

public class ManagerMainScreen extends JFrame {
    private JButton stockManagementButton;
    private JButton reportButton;
    private JButton orderManagementButton;

    public ManagerMainScreen() {
        setTitle("Manager Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Initialize buttons
        stockManagementButton = new JButton("Stock Management");
        reportButton = new JButton("Reports");
        orderManagementButton = new JButton("Order Management");

        // Layout for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.add(stockManagementButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(orderManagementButton);

        // Add to frame
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER);
    }

    public JButton getStockManagementButton() {
        return stockManagementButton;
    }

    public JButton getReportButton() {
        return reportButton;
    }

    public JButton getOrderManagementButton() {
        return orderManagementButton;
    }
}
