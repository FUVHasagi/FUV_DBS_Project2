// CustomerProductPanelController.java
package controller.customer;

import dataAccess.MongoDB;
import dataAccess.MySQL;
import model.Customer;
import model.OrderLine;
import model.Product;
import view.customer.CustomerProductPanel;
import view.layout.BrowseProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerProductPanelController implements ActionListener {
    private CustomerProductPanel view;
    private Customer customer;
    private MySQL mySQL;
    private MongoDB mongoDB;

    public CustomerProductPanelController(Customer customer, MySQL mySQL, MongoDB mongoDB) {
        this.customer = customer;
        this.mySQL = mySQL;
        this.mongoDB = mongoDB;

        BrowseProduct browseProduct = new BrowseProduct(mySQL);
        this.view = new CustomerProductPanel(browseProduct);

        this.view.getAddToCartButton().addActionListener(this);
        this.view.getProductInfoButton().addActionListener(this);

        this.view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddToCartButton()) {
            addProductToCart();
        } else if (e.getSource() == view.getProductInfoButton()) {
            viewProductInfo();
        }
    }

    private void addProductToCart() {
        BrowseProduct browseProduct = (BrowseProduct) view.getBrowserPanel();
        Product selectedProduct = browseProduct.getSelectedProduct();
        if (selectedProduct != null) {
            String quantityStr = JOptionPane.showInputDialog(view, "Enter quantity to add to cart:");
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0) {
                    customer.getCart().addProduct(selectedProduct, quantity);
                    JOptionPane.showMessageDialog(view, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viewProductInfo() {
        BrowseProduct browseProduct = (BrowseProduct) view.getBrowserPanel();
        Product selectedProduct = browseProduct.getSelectedProduct();
        if (selectedProduct != null) {
            new CustomerProductInfoController(selectedProduct, customer, browseProduct);
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
