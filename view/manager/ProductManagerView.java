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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Product Information Panel
        JPanel productInfoPanel = new JPanel(new BorderLayout());
        productInfoPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));
        productInfoPanel.add(productInformationPanel, BorderLayout.CENTER);
        productInformationPanel.getFieldID().setEditable(false);


        // Browse Product Panel
        JPanel browsePanel = new JPanel(new BorderLayout());
        browsePanel.setBorder(BorderFactory.createTitledBorder("Browse Products"));
        browsePanel.add(new JScrollPane(browseProductPanel), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addProductButton = new JButton("Add Product");
        loadProductButton = new JButton("Load Product");
        changeProductButton = new JButton("Change Product");
        deleteProductButton = new JButton("Delete Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(loadProductButton);
        buttonPanel.add(changeProductButton);
        buttonPanel.add(deleteProductButton);


        // Add subpanels to the main panel
        mainPanel.add(productInfoPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Add spacing
        mainPanel.add(browsePanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Add spacing
        mainPanel.add(buttonPanel);

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
