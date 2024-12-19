package dataAccess;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashSet;
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
        MongoCollection<Document> ordersCollection = database.getCollection("orders");
        MongoCollection<Document> customersCollection = database.getCollection("customers");

        // Check if the customer exists and has a valid ID
        if (order.getCustomerID() != null) {
            Document customerDoc = customersCollection.find(Filters.eq("id", order.getCustomerID())).first();
            if (customerDoc == null) {
                throw new IllegalArgumentException("Customer ID not found: " + order.getCustomerID());
            }

            // Update the bought products list for the customer
            List<Integer> boughtProducts = customerDoc.containsKey("boughtProducts")
                    ? (List<Integer>) customerDoc.get("boughtProducts")
                    : new ArrayList<>();

            for (OrderLine line : order.getLines()) {
                if (!boughtProducts.contains(line.getProductID())) {
                    boughtProducts.add(line.getProductID());
                }
            }

            customersCollection.updateOne(
                    Filters.eq("id", order.getCustomerID()),
                    new Document("$set", new Document("boughtProducts", boughtProducts))
            );
        }

        // Save the order
        Document orderDoc = new Document("orderID", order.getOrderID())
                .append("sourceType", order.getSourceType())
                .append("sourceID", order.getSourceID())
                .append("orderDate", order.getOrderDate())
                .append("totalCost", order.getTotalCost())
                .append("orderLines", serializeOrderLines(order.getLines()));

        if (order.getCustomerID() != null) {
            orderDoc.append("customerID", order.getCustomerID());
        }

        ordersCollection.insertOne(orderDoc);
    }


    public List<Order> getOrdersBySource(String sourceType, String sourceID, boolean forCustomer) {
        MongoCollection<Document> collection = database.getCollection("orders");
        List<Order> orders = new ArrayList<>();

        Bson filter;

        if ("all".equalsIgnoreCase(sourceType)) {
            // Match all orders
            filter = Filters.exists("_id");
        } else if (forCustomer && "cashier".equalsIgnoreCase(sourceType)) {
            // Orders placed by cashiers for a specific customer
            filter = Filters.eq("customerID", sourceID);
        } else if (sourceType != null && sourceID != null) {
            // Generic filtering by source type and source ID
            filter = Filters.and(Filters.eq("sourceType", sourceType), Filters.eq("sourceID", sourceID));
        } else {
            return orders; // Return empty for invalid parameters
        }

        // Retrieve and deserialize matching orders
        for (Document doc : collection.find(filter)) {
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
        MongoCollection<Document> reviewsCollection = database.getCollection("reviews");
        MongoCollection<Document> customersCollection = database.getCollection("customers");

        // Save the review
        Document reviewDoc = new Document("productID", review.getProductId())
                .append("customerID", review.getCustomerId())
                .append("rating", review.getRating())
                .append("comment", review.getComment());
        reviewsCollection.insertOne(reviewDoc);

        // Update the customer's reviewed products list
        Document customerDoc = customersCollection.find(Filters.eq("id", review.getCustomerId())).first();
        if (customerDoc != null) {
            List<Integer> reviewedProducts = customerDoc.containsKey("reviewedProducts")
                    ? (List<Integer>) customerDoc.get("reviewedProducts")
                    : new ArrayList<>();
            if (!reviewedProducts.contains(review.getProductId())) {
                reviewedProducts.add(review.getProductId());
                customersCollection.updateOne(
                        Filters.eq("id", review.getCustomerId()),
                        new Document("$set", new Document("reviewedProducts", reviewedProducts))
                );
            }
        }
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

    public Customer ensureCustomerExists(String customerId, String displayName, boolean createNewCustomer) {
        MongoCollection<Document> customersCollection = database.getCollection("customers");

        // Check if the customer already exists
        Document customerDoc = customersCollection.find(Filters.eq("id", customerId)).first();
        if (customerDoc != null) {
            // Deserialize the existing customer
            Customer customer = new Customer(
                    customerDoc.getString("fullName"),
                    customerDoc.getString("id")
            );

            // Initialize or load bought products
            if (customerDoc.containsKey("boughtProducts")) {
                List<Integer> boughtProducts = (List<Integer>) customerDoc.get("boughtProducts");
                customer.setBoughtProducts(new HashSet<>(boughtProducts));
            }

            return customer;
        }
        if (createNewCustomer) {
            // Create a new customer if not found
            Customer newCustomer = new Customer(displayName, customerId);
            newCustomer.setBoughtProducts(new HashSet<>());

            // Save the new customer
            Document newCustomerDoc = new Document("id", customerId)
                    .append("fullName", displayName)
                    .append("boughtProducts", new ArrayList<>());
            customersCollection.insertOne(newCustomerDoc);

            return newCustomer;
        }
        else return null;
    }

    public boolean hasCustomerBoughtProduct(String customerId, int productId) {
        MongoCollection<Document> customersCollection = database.getCollection("customers");

        // Check the bought products list for the customer
        Document customerDoc = customersCollection.find(Filters.eq("id", customerId)).first();
        if (customerDoc != null && customerDoc.containsKey("boughtProducts")) {
            List<Integer> boughtProducts = (List<Integer>) customerDoc.get("boughtProducts");
            return boughtProducts.contains(productId);
        }

        return false;
    }

    public boolean hasCustomerReviewedProduct(String customerId, int productId) {
        MongoCollection<Document> customersCollection = database.getCollection("customers");

        // Check the reviewed products list for the customer
        Document customerDoc = customersCollection.find(Filters.eq("id", customerId)).first();
        if (customerDoc != null && customerDoc.containsKey("reviewedProducts")) {
            List<Integer> reviewedProducts = (List<Integer>) customerDoc.get("reviewedProducts");
            return reviewedProducts.contains(productId);
        }

        return false;
    }


}
