package model;

import java.util.List;

public class Order {
    private List<OrderLine> lines;
    private String customerName;
    private String orderID;
    private String OrderDate;
    private String customerID;
    private float TotalCost;


    public void setOrderID(String orderID) {this.orderID = orderID;}
    public String getOrderID() {return orderID;}

    public void setCustomerID(String customerId) {this.customerID = customerId;}
    public String getCustomerID() {return customerID;}


    public void setOrderDate(String OrderDate) {this.OrderDate = OrderDate;}
    public String getOrderDate() {return OrderDate;}

    public void setTotalCost() {
        calculateTotalCost();
    }

    public float getTotalCost() {return TotalCost;}

    public List<OrderLine> getLines() {return lines;}
    public void setLines(List<OrderLine> lines) {this.lines = lines;}


    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public float calculateTotalCost() {
        this.TotalCost = 0;
        for (OrderLine line : lines){
            this.TotalCost += line.getCost();
        }
        return TotalCost;}
}
