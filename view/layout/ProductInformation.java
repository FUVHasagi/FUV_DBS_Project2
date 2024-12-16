package view.layout;

import model.Product;

import javax.swing.*;

public class ProductInformation {
    private JTextField fieldName;
    private JTextField fieldCategory;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JTextField fieldID;

    public ProductInformation() {}
    public void set_editable(boolean editable) {

    }

    public Product getproduct(){
        Product product = new Product();
        return product;
    }

    public JTextField getFieldName() {
        return fieldName;
    }

}
