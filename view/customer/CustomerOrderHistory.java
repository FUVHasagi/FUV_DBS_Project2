package view.cashier;

import view.layout.BrowseOrderHistory;

import javax.swing.*;
import java.awt.*;

public class CashierOrderHistoryView extends JFrame {
    private BrowseOrderHistory browseOrderHistory;
    private JButton inspectOrderButton;

    public CashierOrderHistoryView() {
        setTitle("Cashier Order History");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderHistory = new BrowseOrderHistory();
        inspectOrderButton = new JButton("Inspect Order");

        // Add components to the frame
        add(browseOrderHistory, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(inspectOrderButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderHistory getBrowseOrderHistory() {
        return browseOrderHistory;
    }

    public JButton getInspectOrderButton() {
        return inspectOrderButton;
    }
}
