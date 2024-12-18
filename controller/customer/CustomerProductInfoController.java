// CustomerProductInfoController.java
package controller.customer;

import dataAccess.MongoDB;
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

    public CustomerProductInfoController(Product product, Customer customer, MongoDB mongoDB) {
        this.product = product;
        this.customer = customer;
        this.mongoDB = mongoDB;
        ProductInformation productInfoPanel = new ProductInformation();
        productInfoPanel.setProduct(product);
        productInfoPanel.setEditable(false);

        this.view = new CustomerProductInfoView(productInfoPanel);

        // Populate initial product reviews
        loadProductReviews();

        // Add action listeners
        this.view.getAddToCartButton().addActionListener(this);
        this.view.getPostReviewButton().addActionListener(this);

        this.view.setVisible(true);
    }

    private void loadProductReviews() {
        StringBuilder reviewsText = new StringBuilder();
        List<Review> reviews = mongoDB.getReviewsByProductId(product.getId());
        for (Review review : reviews) {
            reviewsText.append("Customer: ").append(review.getCustomerId())
                    .append("\nRating: ").append(review.getRating())
                    .append("\nComment: ").append(review.getComment())
                    .append("\n\n");
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
        try {
            OrderLine orderLine = OrderLine.createFromProduct(product, quantity);
            customer.addItemToCart(orderLine);
            JOptionPane.showMessageDialog(view, "Product added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePostReview() {
        if (hasCustomerPurchasedProduct()) {
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
            }
        } else {
            JOptionPane.showMessageDialog(view, "You can only review products you've purchased.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasCustomerPurchasedProduct() {
        List<Order> orders = mongoDB.getOrdersBySource("customer", customer.getId());
        for (Order order : orders) {
            for (OrderLine line : order.getLines()) {
                if (line.getProductID() == product.getId()) {
                    return true;
                }
            }
        }
        return false;
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
}
