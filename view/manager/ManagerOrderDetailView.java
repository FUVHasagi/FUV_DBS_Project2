package view.manager;

import view.layout.BrowseOrderLine;

import javax.swing.*;
import java.awt.*;

public class ManagerOrderDetailView extends JFrame {
    private BrowseOrderLine browseOrderLine;
    private JLabel totalCostLabel;
    private JButton closeButton;

    public ManagerOrderDetailView() {
        setTitle("Order Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderLine = new BrowseOrderLine();
        totalCostLabel = new JLabel("Total Cost: $0.00");
        closeButton = new JButton("Close");

        // South panel for total cost and close button
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(totalCostLabel, BorderLayout.WEST);
        southPanel.add(closeButton, BorderLayout.EAST);

        // Add components to the frame
        add(browseOrderLine, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderLine getBrowseOrderLine() {
        return browseOrderLine;
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public void setTotalCost(double totalCost) {
        totalCostLabel.setText(String.format("Total Cost: $%.2f", totalCost));
    }
}
