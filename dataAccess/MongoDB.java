package dataAccess;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import model.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDB {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDB() {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017/");
        this.database = mongoClient.getDatabase("project2");
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    // Save a customer and their cart
    public void saveCustomer(Customer customer) {
        MongoCollection<Document> collection = database.getCollection("customers");
        Document customerDoc = new Document("id", customer.getId())
                .append("fullName", customer.getFullName())
                .append("cart", serializeCart(customer.getCart()));
        collection.insertOne(customerDoc);
    }

    public Customer getCustomerById(String customerId) {
        MongoCollection<Document> collection = database.getCollection("customers");
        Document doc = collection.find(Filters.eq("id", customerId)).first();
        if (doc != null) {
            Customer customer = new Customer(
                    doc.getString("fullName"),
                    doc.getString("id")
            );
            customer.setCart(deserializeCart((List<Document>) doc.get("cart")));
            return customer;
        }
        return null;
    }

    // Save an order
    public void saveOrder(Order order) {
        MongoCollection<Document> collection = database.getCollection("orders");
        Document orderDoc = new Document("orderID", order.getOrderID())
                .append("sourceType", order.getSourceType()) // "customer" or "cashier"
                .append("sourceID", order.getSourceID())     // Customer or Cashier ID
                .append("orderDate", order.getOrderDate())
                .append("totalCost", order.getTotalCost())
                .append("orderLines", serializeOrderLines(order.getLines()));

        // Add customerID only if it exists
        if (order.getCustomerID() != null) {
            orderDoc.append("customerID", order.getCustomerID());
        }

        collection.insertOne(orderDoc);
    }

    public List<Order> getOrdersBySource(String sourceType, String sourceID) {
        MongoCollection<Document> collection = database.getCollection("orders");
        List<Order> orders = new ArrayList<>();

        for (Document doc : collection.find(Filters.and(
                Filters.eq("sourceType", sourceType),
                Filters.eq("sourceID", sourceID)))) {
            orders.add(deserializeOrder(doc));
        }
        return orders;
    }

    // Deserialize an Order
    private Order deserializeOrder(Document doc) {
        Order order = new Order();
        order.setOrderID(doc.getString("orderID"));
        order.setSourceType(doc.getString("sourceType"));
        order.setSourceID(doc.getString("sourceID"));
        order.setOrderDate(doc.getString("orderDate"));
        order.setTotalCost(doc.getDouble("totalCost"));

        // Set customerID only if it exists in the document
        if (doc.containsKey("customerID")) {
            order.setCustomerID(doc.getString("customerID"));
        }

        order.setLines(deserializeOrderLines((List<Document>) doc.get("orderLines")));
        return order;
    }

    // --- Reviews Management ---

    // Save a product review
    public void saveReview(Review review) {
        MongoCollection<Document> collection = database.getCollection("reviews");
        Document reviewDoc = new Document("productID", review.getProductId())
                .append("customerID", review.getCustomerId())
                .append("rating", review.getRating())
                .append("comment", review.getComment());
        collection.insertOne(reviewDoc);
    }

    // Retrieve reviews for a specific product
    public List<Review> getReviewsByProductId(int productId) {
        MongoCollection<Document> collection = database.getCollection("reviews");
        List<Review> reviews = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("productID", productId))) {
            reviews.add(new Review(
                    doc.getInteger("productID"),
                    doc.getString("customerID"),
                    doc.getDouble("rating"),
                    doc.getString("comment")
            ));
        }
        return reviews;
    }

    // Retrieve average rating for a specific product
    public double getAverageRatingByProductId(int productId) {
        MongoCollection<Document> collection = database.getCollection("reviews");
        double totalRating = 0.0;
        int count = 0;

        for (Document doc : collection.find(Filters.eq("productID", productId))) {
            totalRating += doc.getDouble("rating");
            count++;
        }

        return count > 0 ? totalRating / count : 0.0;
    }

    // --- Serialization and Deserialization Helpers ---

    private List<Document> serializeCart(Cart cart) {
        List<Document> cartItems = new ArrayList<>();
        for (OrderLine orderLine : cart.getItems().values()) {
            cartItems.add(new Document("productID", orderLine.getProductID())
                    .append("productName", orderLine.getProductName())
                    .append("price", orderLine.getPrice())
                    .append("quantity", orderLine.getQuantity())
                    .append("cost", orderLine.getCost()));
        }
        return cartItems;
    }

    private Cart deserializeCart(List<Document> cartDocs) {
        Cart cart = new Cart();
        if (cartDocs != null) {
            for (Document doc : cartDocs) {
                OrderLine orderLine = new OrderLine();
                orderLine.setProductID(doc.getInteger("productID"));
                orderLine.setProductName(doc.getString("productName"));
                orderLine.setPrice(doc.getDouble("price"));
                orderLine.setQuantity(doc.getInteger("quantity"));
                orderLine.setCost();
                cart.addItem(orderLine);
            }
        }
        return cart;
    }

    private List<Document> serializeOrderLines(List<OrderLine> lines) {
        List<Document> orderLines = new ArrayList<>();
        for (OrderLine line : lines) {
            orderLines.add(new Document("productID", line.getProductID())
                    .append("productName", line.getProductName())
                    .append("price", line.getPrice())
                    .append("quantity", line.getQuantity())
                    .append("cost", line.getCost()));
        }
        return orderLines;
    }

    private List<OrderLine> deserializeOrderLines(List<Document> lineDocs) {
        List<OrderLine> lines = new ArrayList<>();
        if (lineDocs != null) {
            for (Document doc : lineDocs) {
                OrderLine orderLine = new OrderLine();
                orderLine.setProductID(doc.getInteger("productID"));
                orderLine.setProductName(doc.getString("productName"));
                orderLine.setPrice(doc.getDouble("price"));
                orderLine.setQuantity(doc.getInteger("quantity"));
                orderLine.setCost();
                lines.add(orderLine);
            }
        }
        return lines;
    }

    public Customer ensureCustomerExists(String customerId, String displayName) {
        // Check if the customer already exists in the MongoDB database
        Customer existingCustomer = getCustomerById(customerId);
        if (existingCustomer != null) {
            // Return the existing customer
            return existingCustomer;
        }

        // If customer doesn't exist, create a new one
        Customer newCustomer = new Customer(displayName, customerId);

        // Save the new customer to the MongoDB database
        saveCustomer(newCustomer);

        // Return the newly created customer
        return newCustomer;
    }
}
