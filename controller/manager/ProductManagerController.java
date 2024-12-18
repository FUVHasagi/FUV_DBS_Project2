package controller.manager;

import dataAccess.MySQL;
import model.Product;
import view.manager.ProductManagerView;
import view.layout.BrowseProduct;
import view.layout.ProductInformation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductManagerController implements ActionListener {
    private ProductManagerView view;
    private MySQL mySQL;

    public ProductManagerController(MySQL mySQL) {
        this.mySQL = mySQL;

        // Initialize subpanels
        BrowseProduct browseProduct = new BrowseProduct(mySQL);
        ProductInformation productInformation = new ProductInformation();

        // Initialize view
        this.view = new ProductManagerView(browseProduct, productInformation);
        this.view.setVisible(true);

        // Add action listeners
        this.view.getAddProductButton().addActionListener(this);
        this.view.getLoadProductButton().addActionListener(this);
        this.view.getChangeProductButton().addActionListener(this);
        this.view.getDeleteProductButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getAddProductButton()) {
            handleAddProduct();
        } else if (e.getSource() == view.getLoadProductButton()) {
            handleLoadProduct();
        } else if (e.getSource() == view.getChangeProductButton()) {
            handleChangeProduct();
        } else if (e.getSource() == view.getDeleteProductButton()) {
            handleDeleteProduct();
        }
    }

    private void handleAddProduct() {
        Product product = view.getProductInformationPanel().getProduct();
        if (product != null) {
            try {
                mySQL.addProduct(product);
                JOptionPane.showMessageDialog(view, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                view.getBrowseProductPanel().updateProductTable();
                view.getProductInformationPanel().clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Failed to add product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLoadProduct() {
        Product selectedProduct = view.getBrowseProductPanel().getSelectedProduct();
        if (selectedProduct != null) {
            view.getProductInformationPanel().setProduct(selectedProduct);
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product to load.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleChangeProduct() {
        Product product = view.getProductInformationPanel().getProduct();
        if (product != null) {
            Product selectedProduct = view.getBrowseProductPanel().getSelectedProduct();
            if (selectedProduct != null && product.getId() == selectedProduct.getId()) {
                try {
                    mySQL.modifyProduct(product);
                    JOptionPane.showMessageDialog(view, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    view.getBrowseProductPanel().updateProductTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Failed to update product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "IDs do not match. Please select the correct product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteProduct() {
        Product selectedProduct = view.getBrowseProductPanel().getSelectedProduct();
        if (selectedProduct != null) {
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    mySQL.deleteProduct(selectedProduct.getId());
                    JOptionPane.showMessageDialog(view, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    view.getBrowseProductPanel().updateProductTable();
                    view.getProductInformationPanel().clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Failed to delete product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
