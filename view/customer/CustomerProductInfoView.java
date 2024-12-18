package view.customer;

import javax.swing.*;
import java.awt.*;

public class CustomerProductInfoView extends JFrame {
    private JButton addToCartButton;
    private JButton postReviewButton;
    private JTextArea readReviewArea;
    private JTextArea writeReviewArea;
    private JPanel panelProduct;
    private JSpinner spinnerQuantity;

    public CustomerProductInfoView(JPanel panelProduct) {
        this.setTitle("Product Information");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());

        this.panelProduct = panelProduct;
        this.add(panelProduct, BorderLayout.NORTH); // Add product panel to the top of the frame

        // Center Panel for Reviews
        JPanel reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Read Review Area
        readReviewArea = new JTextArea();
        readReviewArea.setEditable(false);
        readReviewArea.setLineWrap(true);
        readReviewArea.setWrapStyleWord(true);
        JScrollPane readScrollPane = new JScrollPane(readReviewArea);
        reviewsPanel.add(createTitledPanel("Reviews", readScrollPane));

        // Write Review Area
        writeReviewArea = new JTextArea();
        writeReviewArea.setLineWrap(true);
        writeReviewArea.setWrapStyleWord(true);
        JScrollPane writeScrollPane = new JScrollPane(writeReviewArea);
        reviewsPanel.add(createTitledPanel("Write a Review", writeScrollPane));

        this.add(reviewsPanel, BorderLayout.CENTER);

        // South Panel for Actions
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Quantity Selector
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantityPanel.add(new JLabel("Quantity: "));
        quantityPanel.add(spinnerQuantity);
        actionPanel.add(quantityPanel);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addToCartButton = new JButton("Add to Cart");
        postReviewButton = new JButton("Post Review");
        buttonsPanel.add(addToCartButton);
        buttonsPanel.add(postReviewButton);
        actionPanel.add(buttonsPanel);

        this.add(actionPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public JButton getAddToCartButton() {
        return addToCartButton;
    }

    public JButton getPostReviewButton() {
        return postReviewButton;
    }

    public JTextArea getReadReviewArea() {
        return readReviewArea;
    }

    public JTextArea getWriteReviewArea() {
        return writeReviewArea;
    }

    public JSpinner getSpinnerQuantity() {
        return spinnerQuantity;
    }

    public JPanel getPanelProduct() {
        return panelProduct;
    }
}
