package view.cashier;

import view.layout.BrowseOrderLine;

import javax.swing.*;
import java.awt.*;

public class CashierOrderDetailView extends JFrame {
    private BrowseOrderLine browseOrderLinePanel;
    private JButton closeButton;

    public CashierOrderDetailView() {
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        browseOrderLinePanel = new BrowseOrderLine();
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());
        add(browseOrderLinePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public BrowseOrderLine getBrowseOrderLine() {
        return browseOrderLinePanel;
    }

    public JButton getCloseButton() {
        return closeButton;
    }
}
