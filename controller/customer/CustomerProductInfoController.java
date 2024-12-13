package controller.customer;

import model.Customer;
import model.OrderLine;
import model.Product;
import view.customer.CustomerProductInfoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerProductInfoController implements ActionListener {
    private CustomerProductInfoView view;
    private Product product;
    private Customer customer;

    public CustomerProductInfoController(Product product, Customer customer, JPanel panelProduct) {
        this.product = product;
        this.customer = customer;
        this.view = new CustomerProductInfoView(panelProduct);

        // Populate initial product reviews
        loadProductReviews();

        // Add action listeners
        this.view.getAddToCartButton().addActionListener(this);
        this.view.getPostReviewButton().addActionListener(this);

        this.view.setVisible(true);
    }

    private void loadProductReviews() {
        // This is a placeholder. You'd retrieve reviews from the database or model.
        String dummyReviews = "Review 1: Great product!\nReview 2: Worth the price.\n";
        view.getReadReviewArea().setText(dummyReviews);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddToCartButton()) {
            int quantity = (int) view.getSpinnerQuantity().getValue();
            try {
                OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
                customer.addItemToCart(orderLine);
                JOptionPane.showMessageDialog(view, "Product added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == view.getPostReviewButton()) {
            if (hasCustomerPurchasedProduct()) {
                String review = view.getWriteReviewArea().getText();
                if (review.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Review cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    postReview(review);
                }
            } else {
                JOptionPane.showMessageDialog(view, "You can only review products you've purchased.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean hasCustomerPurchasedProduct() {
        // Placeholder: Check if the customer has purchased the product
        // Ideally, this checks purchase history in the database or model.
        return true; // Assume customer has purchased for now.
    }

    private void postReview(String review) {
        // Placeholder: Save the review to the database or model.
        view.getReadReviewArea().append("Customer Review: " + review + "\n");
        view.getWriteReviewArea().setText("");
        JOptionPane.showMessageDialog(view, "Review posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
