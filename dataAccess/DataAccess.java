package dataAccess;

import model.Customer;
import model.Order;
import model.Product;
import model.Review; // Make sure you have a Review model

import java.util.List;

public interface DataAccess {
    // MySQL methods
    Product getProductById(int productId);
    // ... other MySQL methods for products and users

    // MongoDB methods
    void saveCustomer(Customer customer);
    Customer getCustomerById(String customerId);
    void saveOrder(Order order);
    List<Order> getOrdersByCustomerId(String customerId);
    void saveReview(Review review);
    List<Review> getReviewsByProductId(int productId);
    // ... other MongoDB methods

    // Redis methods
    List<Product> getBestSellingProducts(int count);
    List<Customer> getRecentCustomers(int count);
    // ... other Redis methods
}