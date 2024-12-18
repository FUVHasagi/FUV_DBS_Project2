package view.manager;

import view.layout.BrowseProduct;
import view.layout.ProductInformation;

import javax.swing.*;
import java.awt.*;

public class ProductManagerView extends JFrame {
    private BrowseProduct browseProductPanel;
    private ProductInformation productInformationPanel;
    private JButton addProductButton;
    private JButton loadProductButton;
    private JButton changeProductButton;
    private JButton deleteProductButton;

    public ProductManagerView(BrowseProduct browseProductPanel, ProductInformation productInformationPanel) {
        this.browseProductPanel = browseProductPanel;
        this.productInformationPanel = productInformationPanel;

        setTitle("Product Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Initialize buttons
        addProductButton = new JButton("Add Product");
        loadProductButton = new JButton("Load Product");
        changeProductButton = new JButton("Change Product");
        deleteProductButton = new JButton("Delete Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(loadProductButton);
        buttonPanel.add(changeProductButton);
        buttonPanel.add(deleteProductButton);

        // Add subpanels and button panel to the main panel
        mainPanel.add(new JScrollPane(browseProductPanel), BorderLayout.CENTER);
        mainPanel.add(productInformationPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public JButton getLoadProductButton() {
        return loadProductButton;
    }

    public JButton getChangeProductButton() {
        return changeProductButton;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }

    public BrowseProduct getBrowseProductPanel() {
        return browseProductPanel;
    }

    public ProductInformation getProductInformationPanel() {
        return productInformationPanel;
    }
}
