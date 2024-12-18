package view.customer;

import view.layout.BrowseOrderLine;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class CartView extends JFrame {
    private BrowseOrderLine browseOrderLine; // Use BrowseOrderLine
    private JLabel totalCostLabel;
    private JButton deleteFromCartButton;
    private JButton changeQuantityButton;
    private JButton finishAndPayButton;

    public CartView() {
        setTitle("Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderLine = new BrowseOrderLine();
        totalCostLabel = new JLabel("Total Cost: $0.00");
        totalCostLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalCostLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        deleteFromCartButton = new JButton("Delete Item");
        changeQuantityButton = new JButton("Change Quantity");
        finishAndPayButton = new JButton("Finish & Pay");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(deleteFromCartButton);
        buttonPanel.add(changeQuantityButton);
        buttonPanel.add(finishAndPayButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(totalCostLabel, BorderLayout.EAST);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add components to frame
        add(browseOrderLine, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getters for components
    public BrowseOrderLine getBrowseOrderLine() {
        return browseOrderLine;
    }

    public JLabel getTotalCostLabel() {
        return totalCostLabel;
    }

    public JButton getDeleteFromCartButton() {
        return deleteFromCartButton;
    }

    public JButton getChangeQuantityButton() {
        return changeQuantityButton;
    }

    public JButton getFinishAndPayButton() {
        return finishAndPayButton;
    }

    // Helper method to update the total cost label
    public void updateTotalCost(double totalCost) {
        DecimalFormat df = new DecimalFormat("0.00");
        totalCostLabel.setText("Total Cost: $" + df.format(totalCost));
    }
}
