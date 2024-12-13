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

        this.browserPanel = browseProduct;
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
