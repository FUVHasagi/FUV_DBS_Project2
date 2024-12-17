package model;

public class Product {
    private int id;
    private String name;
    private int stock_quantity;
    private double sellPrice;
    private String category;
    private String brand; // Added Brand Field

    public Product(int id, String name, double sellPrice, int stock_quantity, String category, String brand) {
        this.id = id;
        this.name = name;
        this.stock_quantity = stock_quantity;
        this.sellPrice = sellPrice;
        this.category = category;
        this.brand = brand;
    }

    public Product() {
        this.id = -1;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStockQuantity() { return stock_quantity; }
    public void setStockQuantity(int stock_quantity) { this.stock_quantity = stock_quantity; }

    public double getSellPrice() { return sellPrice; }
    public void setSellPrice(double sellPrice) { this.sellPrice = sellPrice; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
