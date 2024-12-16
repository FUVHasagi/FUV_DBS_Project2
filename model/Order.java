package model;

import java.util.List;

public class Order {
    private String orderID;
    private String customerID;
    private String customerName;
    private String orderDate;
    private List<OrderLine> lines;
    private float totalCost;

    // Getters and Setters
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void setLines(List<OrderLine> lines) {
        this.lines = lines;
        calculateTotalCost();
    }

    public float getTotalCost() {
        return totalCost;
    }

    private void calculateTotalCost() {
        this.totalCost = 0;
        for (OrderLine line : lines) {
            this.totalCost += line.getCost();
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", customerName='" + customerName + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", lines=" + lines +
                ", totalCost=" + totalCost +
                '}';
    }
}
