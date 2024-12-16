package model;

public class OrderLine {
    private int productID;
    private String productName;
    private double price;
    private int quantity;
    private double cost;

    public static OrderLine createFromProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity.");
        }

        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        OrderLine orderLine = new OrderLine();
        orderLine.setProductID(product.getId());
        orderLine.setProductName(product.getName());
        orderLine.setPrice(product.getSellPrice());
        orderLine.setQuantity(quantity);
        orderLine.calculateCost(); // Automatically calculates cost

        return orderLine;
    }

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateCost();
    }

    public double getCost() {
        return cost;
    }

    private void calculateCost() {
        this.cost = this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
