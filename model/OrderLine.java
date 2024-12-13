package model;

public class OrderLine {
    private int productID;
    private int orderID;
    private String productName;
    private double price;
    private int quantity;
    private double cost;

    // Static initialization function
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
        orderLine.setCost(); // Automatically calculates cost

        return orderLine;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost() {
        this.cost = price*quantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
