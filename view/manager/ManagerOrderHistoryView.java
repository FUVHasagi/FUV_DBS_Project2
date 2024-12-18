package view.manager;

import view.layout.BrowseOrderHistory;

import javax.swing.*;
import java.awt.*;

public class ManagerOrderHistoryView extends JFrame {
    private BrowseOrderHistory browseOrderHistory;
    private JButton showAllButton;
    private JButton inspectOrderButton;

    public ManagerOrderHistoryView() {
        setTitle("Manager Order History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Initialize components
        browseOrderHistory = new BrowseOrderHistory();
        browseOrderHistory.setManagerMode(true); // Enable manager mode

        showAllButton = new JButton("Show All Orders");
        inspectOrderButton = new JButton("Inspect Order");

        // Add components
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(showAllButton);
        buttonPanel.add(inspectOrderButton);

        add(browseOrderHistory, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderHistory getBrowseOrderHistory() {
        return browseOrderHistory;
    }

    public JButton getShowAllButton() {
        return showAllButton;
    }

    public JButton getInspectOrderButton() {
        return inspectOrderButton;
    }
}
