package view.layout;

import model.Product;

import javax.swing.*;
import java.awt.*;

public class ProductInformation extends JPanel {
    private JTextField fieldName;
    private JTextField fieldCategory;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JTextField fieldID;
    private JTextField fieldBrand;

    public ProductInformation() {
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Product ID:"));
        fieldID = new JTextField();
        fieldID.setEditable(false); // ID is always non-editable
        add(fieldID);

        add(new JLabel("Name:"));
        fieldName = new JTextField();
        add(fieldName);

        add(new JLabel("Category:"));
        fieldCategory = new JTextField();
        add(fieldCategory);

        add(new JLabel("Price:"));
        fieldPrice = new JTextField();
        add(fieldPrice);

        add(new JLabel("Stock:"));
        fieldStock = new JTextField();
        add(fieldStock);

        add(new JLabel("Brand:"));
        fieldBrand = new JTextField();
        add(fieldBrand);
    }

    public void setEditable(boolean editable) {
        // Only allow the manager to edit product information
        fieldName.setEditable(editable);
        fieldCategory.setEditable(editable);
        fieldPrice.setEditable(editable);
        fieldStock.setEditable(editable);
        fieldBrand.setEditable(editable);
    }

    public void setProduct(Product product) {
        if (product != null) {
            fieldID.setText(String.valueOf(product.getId()));
            fieldName.setText(product.getName());
            fieldCategory.setText(product.getCategory());
            fieldPrice.setText(String.valueOf(product.getSellPrice()));
            fieldStock.setText(String.valueOf(product.getStockQuantity()));
            fieldBrand.setText(product.getBrand());
        }
    }

    public Product getProduct() {
        try {
            int id = Integer.parseInt(fieldID.getText().trim());
            String name = fieldName.getText().trim();
            String category = fieldCategory.getText().trim();
            double price = Double.parseDouble(fieldPrice.getText().trim());
            int stock = Integer.parseInt(fieldStock.getText().trim());
            String brand = fieldBrand.getText().trim();

            return new Product(id, name, price, stock, category, brand);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please ensure all fields are correctly filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void clearFields() {
        fieldID.setText("");
        fieldName.setText("");
        fieldCategory.setText("");
        fieldPrice.setText("");
        fieldStock.setText("");
        fieldBrand.setText("");
    }

    public JTextField getFieldName() {
        return fieldName;
    }

    public JTextField getFieldCategory() {
        return fieldCategory;
    }

    public JTextField getFieldPrice() {
        return fieldPrice;
    }

    public JTextField getFieldStock() {
        return fieldStock;
    }

    public JTextField getFieldID() {
        return fieldID;
    }

    public JTextField getFieldBrand() {
        return fieldBrand;
    }
}
