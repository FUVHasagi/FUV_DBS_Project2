// CustomerProductInfoController.java
package controller.customer;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.Product;
import model.Review;
import view.customer.CustomerProductInfoView;
import view.layout.ProductInformation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerProductInfoController implements ActionListener {
    private CustomerProductInfoView view;
    private Product product;
    private Customer customer;
    private MongoDB mongoDB;
    private MySQL mySQL;

    public CustomerProductInfoController(Product product, Customer customer, MongoDB mongoDB, MySQL mySQL) {
        this.product = product;
        this.customer = customer;
        this.mongoDB = mongoDB;
        this.mySQL = mySQL;

        ProductInformation productInfoPanel = new ProductInformation();
        productInfoPanel.setProduct(product);
        productInfoPanel.setEditable(false);
        productInfoPanel.getFieldID().setEditable(false);

        this.view = new CustomerProductInfoView(productInfoPanel);

        // Check eligibility for review
        checkReviewEligibility();

        // Add action listeners
        this.view.getAddToCartButton().addActionListener(this);
        this.view.getPostReviewButton().addActionListener(this);

        this.view.setVisible(true);

        // Populate initial product reviews
        loadProductReviews();
    }

    private void loadProductReviews() {
        StringBuilder reviewsText = new StringBuilder();
        List<Review> reviews = mongoDB.getReviewsByProductId(product.getId());

        if (reviews == null || reviews.isEmpty()) {
            // No reviews found for the product
            reviewsText.append("There are no reviews for this product yet.");
        } else {
            for (Review review : reviews) {
                reviewsText.append("Customer: ").append(review.getCustomerId())
                        .append("\nRating: ").append(review.getRating())
                        .append("\nComment: ").append(review.getComment())
                        .append("\n\n");
            }
        }

        view.getReadReviewArea().setText(reviewsText.toString());
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddToCartButton()) {
            handleAddToCart();
        } else if (e.getSource() == view.getPostReviewButton()) {
            handlePostReview();
        }
    }

    private void handleAddToCart() {
        int quantity = (int) view.getSpinnerQuantity().getValue();

        // Check if the quantity is valid
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(view, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check stock sufficiency
        boolean isStockSufficient = mySQL.isStockSufficient(product.getId(), quantity);
        if (!isStockSufficient) {
            JOptionPane.showMessageDialog(view, "Insufficient stock for the selected product.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Add product to the cart
            OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
            customer.addItemToCart(orderLine);


            JOptionPane.showMessageDialog(view, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handlePostReview() {
        String reviewText = view.getWriteReviewArea().getText().trim();
        if (reviewText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Review cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rating = getRatingFromUser();
        if (rating > 0) {
            Review review = new Review(product.getId(), customer.getId(), rating, reviewText);
            mongoDB.saveReview(review);
            loadProductReviews();
            view.getWriteReviewArea().setText("");
            JOptionPane.showMessageDialog(view, "Review posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Disable review inputs after posting
            view.getPostReviewButton().setEnabled(false);
            view.getWriteReviewArea().setEnabled(false);
        }
    }

    private int getRatingFromUser() {
        SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 5, 1);
        JSpinner spinner = new JSpinner(model);
        int result = JOptionPane.showConfirmDialog(view, spinner, "Select Rating (1-5)", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return (int) spinner.getValue();
        }
        return -1; // Indicate user canceled
    }

    private void checkReviewEligibility() {
        boolean hasBought = mongoDB.hasCustomerBoughtProduct(customer.getId(), product.getId());
        boolean hasReviewed = mongoDB.hasCustomerReviewedProduct(customer.getId(), product.getId());

        if (!hasBought || hasReviewed) {
            view.getPostReviewButton().setEnabled(false);
            view.getWriteReviewArea().setEnabled(false);

            String message = !hasBought
                    ? "You can only review products you've purchased."
                    : "You have already reviewed this product.";
            JOptionPane.showMessageDialog(view, message, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
