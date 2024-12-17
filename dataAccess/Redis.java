package dataAccess;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import model.Customer;
import model.Product;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

public class Redis {
    private JedisPool jedisPool;
    private MongoDatabase mongoDatabase;

    public Redis(MongoDB mongoDB) {
        // Initialize Jedis connection pool
        this.jedisPool = new JedisPool(new JedisPoolConfig(),
                "redis-15530.c252.ap-southeast-1-1.ec2.redns.redis-cloud.com", 15530);

        // MongoDB reference
        this.mongoDatabase = mongoDB.getDatabase();
    }

    // --- Top 3 Best-Selling Products ---
    public List<Document> getTopSellingProducts() {
        return getCachedData("top_selling_products", () -> {
            MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");

            return ordersCollection.aggregate(Arrays.asList(
                    Aggregates.unwind("$lines"),
                    Aggregates.group("$lines.productID", Accumulators.sum("totalQuantity", "$lines.quantity")),
                    Aggregates.sort(Sorts.descending("totalQuantity")),
                    Aggregates.limit(3)
            )).into(new ArrayList<>());
        });
    }

    // --- Top 3 Best-Selling Categories ---
    public List<Document> getTopSellingCategories() {
        return getCachedData("top_selling_categories", () -> {
            MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");

            return ordersCollection.aggregate(Arrays.asList(
                    Aggregates.unwind("$lines"),
                    Aggregates.lookup("products", "lines.productID", "ID", "productDetails"),
                    Aggregates.unwind("$productDetails"),
                    Aggregates.group("$productDetails.category", Accumulators.sum("totalQuantity", "$lines.quantity")),
                    Aggregates.sort(Sorts.descending("totalQuantity")),
                    Aggregates.limit(3)
            )).into(new ArrayList<>());
        });
    }

    // --- Top 3 Most Income Products ---
    public List<Document> getTopIncomeProducts() {
        return getCachedData("top_income_products", () -> {
            MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");

            return ordersCollection.aggregate(Arrays.asList(
                    Aggregates.unwind("$lines"),
                    Aggregates.group("$lines.productID", Accumulators.sum("totalIncome",
                            new Document("$multiply", Arrays.asList("$lines.price", "$lines.quantity")))),
                    Aggregates.sort(Sorts.descending("totalIncome")),
                    Aggregates.limit(3)
            )).into(new ArrayList<>());
        });
    }

    // --- Top 3 Most Income Customers ---
    public List<Document> getTopIncomeCustomers() {
        return getCachedData("top_income_customers", () -> {
            MongoCollection<Document> ordersCollection = mongoDatabase.getCollection("orders");

            return ordersCollection.aggregate(Arrays.asList(
                    Aggregates.group("$customerID", Accumulators.sum("totalSpent", "$totalCost")),
                    Aggregates.sort(Sorts.descending("totalSpent")),
                    Aggregates.limit(3)
            )).into(new ArrayList<>());
        });
    }

    // --- Helper Method for Caching ---
    private List<Document> getCachedData(String cacheKey, DataFetcher fetcher) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(cacheKey)) {
                System.out.println("Cache hit for " + cacheKey);
                return deserializeProductList(jedis.lrange(cacheKey, 0, -1));
            }
        }

        System.out.println("Cache miss. Fetching " + cacheKey + " from MongoDB...");
        List<Document> result = fetcher.fetch();

        // Cache the results
        try (Jedis jedis = jedisPool.getResource()) {
            for (Document doc : result) {
                jedis.rpush(cacheKey, doc.toJson());
            }
            jedis.expire(cacheKey, 600); // 10-minute expiry
        }

        return result;
    }

    private List<Document> deserializeProductList(List<String> jsonList) {
        List<Document> products = new ArrayList<>();
        for (String json : jsonList) {
            products.add(Document.parse(json));
        }
        return products;
    }

    @FunctionalInterface
    interface DataFetcher {
        List<Document> fetch();
    }
}
