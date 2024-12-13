package controller.customer;

import model.Customer;
import model.Product;
import view.customer.CustomerProductPanel;
import view.layout.BrowseProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerProductPanelController implements ActionListener {
    private CustomerProductPanel view;
    private Customer customer;

    public CustomerProductPanelController(Customer customer, BrowseProduct browseProduct) {
        this.customer = customer;
        this.view = new CustomerProductPanel(browseProduct);
        this.view.getAddToCartButton().addActionListener(this);
        this.view.getProductInfoButton().addActionListener(this);

        this.view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddToCartButton()) {
            // Add the selected product to the customer's cart
            Product selectedProduct = ((BrowseProduct) view.getBrowserPanel()).getSelectedProduct();
            if (selectedProduct != null) {
                customer.getCart().addProduct(selectedProduct, 1); // Assuming `addProduct` method in `Cart` class
                JOptionPane.showMessageDialog(view, "Product added to cart successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == view.getProductInfoButton()) {
            // Open the product info view
            Product selectedProduct = ((BrowseProduct) view.getBrowserPanel()).getSelectedProduct();
            if (selectedProduct != null) {
                new CustomerProductInfoController(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(view, "Please select a product first.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
