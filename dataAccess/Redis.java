package dataAccess;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import model.Order;
import org.bson.Document;
import redis.clients.jedis.*;

import java.util.*;
import java.util.function.Supplier;

public class Redis {
    private UnifiedJedis jedisClient;
    private MongoDatabase mongoDatabase;
    private static final int CACHE_EXPIRY_SECONDS = 600; // 10 minutes
    private static final Gson gson = new Gson();

    public Redis(MongoDB mongoDB) {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user("default")
                .password("ToDFk4ozG6OJQ7rlR0CIfmPiRXmYUr9U")
                .build();
        this.jedisClient = new UnifiedJedis(
                new HostAndPort("redis-15530.c252.ap-southeast-1-1.ec2.redns.redis-cloud.com", 15530),
                config
        );
        this.mongoDatabase = mongoDB.getDatabase();
    }

    // --- Methods for ManagerReportController ---
    public List<Document> getTopSellingProducts() {
        return getCachedData("top_selling_products", this::fetchTopSellingProducts);
    }

    public List<Document> getTopSellingCategories() {
        return getCachedData("top_selling_categories", this::fetchTopSellingCategories);
    }

    public List<Document> getTopIncomeProducts() {
        return getCachedData("top_income_products", this::fetchTopIncomeProducts);
    }

    public List<Document> getTopIncomeCustomers() {
        return getCachedData("top_income_customers", this::fetchTopIncomeCustomers);
    }

    // --- Methods for ManagerOrderHistoryController ---
    public List<Order> getCachedOrders(String cacheKey) {
        List<String> cachedOrders = jedisClient.lrange(cacheKey, 0, -1);
        List<Order> orders = new ArrayList<>();
        for (String json : cachedOrders) {
            orders.add(gson.fromJson(json, Order.class));
        }
        return orders;
    }

    public void cacheOrders(String cacheKey, List<Order> orders) {
        jedisClient.del(cacheKey);
        for (Order order : orders) {
            jedisClient.rpush(cacheKey, gson.toJson(order));
        }
        jedisClient.expire(cacheKey, CACHE_EXPIRY_SECONDS);
    }

    public void reloadCache(String cacheKey) {
        switch (cacheKey) {
            case "top_selling_products":
                cacheData(cacheKey, fetchTopSellingProducts());
                break;
            case "top_selling_categories":
                cacheData(cacheKey, fetchTopSellingCategories());
                break;
            case "top_income_products":
                cacheData(cacheKey, fetchTopIncomeProducts());
                break;
            case "top_income_customers":
                cacheData(cacheKey, fetchTopIncomeCustomers());
                break;
            default:
                throw new IllegalArgumentException("Invalid cache key: " + cacheKey);
        }
    }

    // --- MongoDB Fetch Methods ---
    private List<Document> fetchTopSellingProducts() {
        MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");
        return ordersCollection.aggregate(Arrays.asList(
                Aggregates.unwind("$lines"),
                Aggregates.group("$lines.productID", Accumulators.sum("totalQuantity", "$lines.quantity")),
                Aggregates.sort(Sorts.descending("totalQuantity")),
                Aggregates.limit(3)
        )).into(new ArrayList<>());
    }

    private List<Document> fetchTopSellingCategories() {
        MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");
        return ordersCollection.aggregate(Arrays.asList(
                Aggregates.unwind("$lines"),
                Aggregates.lookup("products", "lines.productID", "ID", "productDetails"),
                Aggregates.unwind("$productDetails"),
                Aggregates.group("$productDetails.category", Accumulators.sum("totalQuantity", "$lines.quantity")),
                Aggregates.sort(Sorts.descending("totalQuantity")),
                Aggregates.limit(3)
        )).into(new ArrayList<>());
    }

    private List<Document> fetchTopIncomeProducts() {
        MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");
        return ordersCollection.aggregate(Arrays.asList(
                Aggregates.unwind("$lines"),
                Aggregates.group("$lines.productID", Accumulators.sum("totalIncome",
                        new Document("$multiply", Arrays.asList("$lines.price", "$lines.quantity")))),
                Aggregates.sort(Sorts.descending("totalIncome")),
                Aggregates.limit(3)
        )).into(new ArrayList<>());
    }

    private List<Document> fetchTopIncomeCustomers() {
        MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");
        return ordersCollection.aggregate(Arrays.asList(
                Aggregates.group("$customerID", Accumulators.sum("totalSpent", "$totalCost")),
                Aggregates.sort(Sorts.descending("totalSpent")),
                Aggregates.limit(3)
        )).into(new ArrayList<>());
    }

    private List<Document> getCachedData(String cacheKey, Supplier<List<Document>> dataSupplier) {
        if (jedisClient.exists(cacheKey)) {
            return deserializeDocumentList(jedisClient.lrange(cacheKey, 0, -1));
        }
        List<Document> data = dataSupplier.get();
        cacheData(cacheKey, data);
        return data;
    }

    private void cacheData(String cacheKey, List<Document> data) {
        jedisClient.del(cacheKey);
        for (Document doc : data) {
            jedisClient.rpush(cacheKey, doc.toJson());
        }
        jedisClient.expire(cacheKey, CACHE_EXPIRY_SECONDS);
    }

    private List<Document> deserializeDocumentList(List<String> jsonList) {
        List<Document> documents = new ArrayList<>();
        for (String json : jsonList) {
            documents.add(Document.parse(json));
        }
        return documents;
    }

    public void close() {
        if (jedisClient != null) {
            jedisClient.close();
        }
    }
}
