package dataAccess;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import model.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDB implements DataAccess {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDB() {
        // Initialize MongoDB connection
        String connectionString = "mongodb://localhost:27017"; // Replace with your connection string
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("store_management"); // Use a consistent database name
    }

    // Save a customer to the MongoDB collection
    @Override
    public void saveCustomer(Customer customer) {
        MongoCollection<Document> collection = database.getCollection("customers");
        Document customerDoc = new Document("id", customer.getId())
                .append("fullName", customer.getFullName())
                .append("cart", serializeCart(customer.getCart()));
        collection.insertOne(customerDoc);
    }

    // Retrieve a customer by ID
    @Override
    public Customer getCustomerById(String customerId) {
        MongoCollection<Document> collection = database.getCollection("customers");
        Document doc = collection.find(Filters.eq("id", customerId)).first();

        if (doc != null) {
            return new Customer(
                    doc.getString("fullName"),
                    doc.getString("id")
            );
        }
        return null;
    }

    // Save an order to MongoDB
    @Override
    public void saveOrder(Order order) {
        MongoCollection<Document> collection = database.getCollection("orders");
        Document orderDoc = new Document("orderID", order.getOrderID())
                .append("customerID", order.getCustomerID())
                .append("orderDate", order.getOrderDate())
                .append("totalCost", order.getTotalCost())
                .append("lines", serializeOrderLines(order.getLines()));
        collection.insertOne(orderDoc);
    }

    // Retrieve orders by customer ID
    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        MongoCollection<Document> collection = database.getCollection("orders");
        List<Order> orders = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("customerID", customerId))) {
            Order order = new Order();
            order.setOrderID(doc.getString("orderID"));
            order.setCustomerID(doc.getString("customerID"));
            order.setOrderDate(doc.getString("orderDate"));
            order.setTotalCost((double)doc.getDouble("totalCost"));
            order.setLines(deserializeOrderLines((List<Document>) doc.get("lines")));
            orders.add(order);
        }
        return orders;
    }

    // Save a product review
    @Override
    public void saveReview(Review review) {
        MongoCollection<Document> collection = database.getCollection("reviews");
        Document reviewDoc = new Document("productID", review.getProductId())
                .append("customerID", review.getCustomerId())
                .append("rating", review.getRating())
                .append("comment", review.getComment());
        collection.insertOne(reviewDoc);
    }

    // Retrieve reviews by product ID
    @Override
    public List<Review> getReviewsByProductId(int productId) {
        MongoCollection<Document> collection = database.getCollection("reviews");
        List<Review> reviews = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("productID", productId))) {
            Review review = new Review(
                    doc.getInteger("productID"),
                    doc.getString("customerID"),
                    doc.getDouble("rating"),
                    doc.getString("comment")
            );
            reviews.add(review);
        }
        return reviews;
    }

    // Serialize a cart object to a MongoDB-friendly format
    private List<Document> serializeCart(Cart cart) {
        List<Document> cartItems = new ArrayList<>();
        for (OrderLine orderLine : cart.getItems().values()) {
            cartItems.add(new Document("productID", orderLine.getProductID())
                    .append("productName", orderLine.getProductName())
                    .append("quantity", orderLine.getQuantity())
                    .append("price", orderLine.getPrice())
                    .append("cost", orderLine.getCost()));
        }
        return cartItems;
    }

    // Serialize order lines to MongoDB-friendly format
    private List<Document> serializeOrderLines(List<OrderLine> lines) {
        List<Document> orderLines = new ArrayList<>();
        for (OrderLine line : lines) {
            orderLines.add(new Document("productID", line.getProductID())
                    .append("productName", line.getProductName())
                    .append("quantity", line.getQuantity())
                    .append("price", line.getPrice())
                    .append("cost", line.getCost()));
        }
        return orderLines;
    }

    // Deserialize order lines from MongoDB format
    private List<OrderLine> deserializeOrderLines(List<Document> docs) {
        List<OrderLine> lines = new ArrayList<>();
        for (Document doc : docs) {
            OrderLine line = new OrderLine();
            line.setProductID(doc.getInteger("productID"));
            line.setProductName(doc.getString("productName"));
            line.setQuantity(doc.getInteger("quantity"));
            line.setPrice(doc.getDouble("price"));
            line.setCost(); // Automatically calculates cost
            lines.add(line);
        }
        return lines;
    }
}
