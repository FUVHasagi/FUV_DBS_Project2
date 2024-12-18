package view.cashier;

import view.layout.BrowseProduct;

import javax.swing.*;
import java.awt.*;

public class CashierProductCheckView extends JFrame {
    private JButton inspectProductButton;
    private BrowseProduct browsePanel;

    public CashierProductCheckView(BrowseProduct browseProduct) {
        setTitle("Cashier Product Check");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        this.browsePanel = browseProduct;
        initializeComponents();
    }

    private void initializeComponents() {
        inspectProductButton = new JButton("Inspect Product");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(inspectProductButton);

        this.setLayout(new BorderLayout());
        this.add(browsePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getInspectProductButton() {
        return inspectProductButton;
    }

    public JPanel getBrowsePanel() {
        return browsePanel;
    }
}
