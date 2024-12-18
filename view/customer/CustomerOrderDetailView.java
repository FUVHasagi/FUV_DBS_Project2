package view.customer;

import view.layout.BrowseOrderLine;

import javax.swing.*;
import java.awt.*;

public class CustomerOrderDetailView extends JFrame {
    private BrowseOrderLine browseOrderLine;
    private JButton reviewProductButton;
    private JButton buyAgainButton;

    public CustomerOrderDetailView() {
        setTitle("Order Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderLine = new BrowseOrderLine();
        reviewProductButton = new JButton("Review Product");
        buyAgainButton = new JButton("Buy Again");

        // Add components to the frame
        add(browseOrderLine, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(reviewProductButton);
        buttonPanel.add(buyAgainButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderLine getBrowseOrderLine() {
        return browseOrderLine;
    }

    public JButton getReviewProductButton() {
        return reviewProductButton;
    }

    public JButton getBuyAgainButton() {
        return buyAgainButton;
    }
}
