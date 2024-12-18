package view.cashier;

import view.layout.BrowseOrderHistory;

import javax.swing.*;
import java.awt.*;

public class CashierOrderHistoryView extends JFrame {
    private BrowseOrderHistory browseOrderHistory;
    private JButton inspectOrderButton;

    public CashierOrderHistoryView() {
        setTitle("Cashier Order History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderHistory = new BrowseOrderHistory();
        inspectOrderButton = new JButton("Inspect Order");

        // Layout components
        add(browseOrderHistory, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(inspectOrderButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderHistory getBrowseOrderHistory() {
        return browseOrderHistory;
    }

    public JButton getInspectOrderButton() {
        return inspectOrderButton;
    }
}
