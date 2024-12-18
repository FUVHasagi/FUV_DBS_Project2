// CustomerProductPanel.java
package view.customer;

import view.layout.BrowseProduct;
import javax.swing.*;
import java.awt.*;

public class CustomerProductPanel extends JFrame {
    private JButton addToCartButton;
    private JButton productInfoButton;
    private JPanel browserPanel;

    public CustomerProductPanel(BrowseProduct browseProduct) {
        this.setTitle("Product Information");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());

        // Initialize components
        this.browserPanel = browseProduct;
        initializeComponents();
    }

    private void initializeComponents() {
        addToCartButton = new JButton("Add to Cart");
        productInfoButton = new JButton("View Product Info");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addToCartButton);
        buttonPanel.add(productInfoButton);

        this.add(browserPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getAddToCartButton() {
        return addToCartButton;
    }

    public JButton getProductInfoButton() {
        return productInfoButton;
    }

    public JPanel getBrowserPanel() {
        return browserPanel;
    }
}
