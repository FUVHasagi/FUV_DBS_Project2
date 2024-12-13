package model;

public class Product {
    private int id;
    private String name;
    private int stock_quantity; // Renamed from quantity
    private double sellPrice;
    private String category;

    public Product(int id, String name, double sellPrice, int stock_quantity, String category) {
        this.id = id;
        this.name = name;
        this.stock_quantity = stock_quantity;
        this.sellPrice = sellPrice;
        this.category =  category;
    }

    public Product() {
        this.id = -1;
        this.name = null;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStockQuantity(int stock_quantity) { // Updated setter
        this.stock_quantity = stock_quantity;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStockQuantity() { // Updated getter
        return stock_quantity;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
