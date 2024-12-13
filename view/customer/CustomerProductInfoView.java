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