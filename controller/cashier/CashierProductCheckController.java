package controller.cashier;

import dataAccess.MySQL;
import model.Product;
import model.User;
import view.cashier.CashierProductCheckView;
import view.layout.BrowseProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierProductCheckController implements ActionListener {
    private CashierProductCheckView view;
    private BrowseProduct browseProductPanel;
    private User cashier;
    private MySQL mySQL;

    public CashierProductCheckController(User cashier, MySQL mySQL) {
        this.cashier = cashier;
        this.mySQL = mySQL;

        browseProductPanel = new BrowseProduct(mySQL);
        view = new CashierProductCheckView(browseProductPanel);

        initializeListeners();

        view.setVisible(true);

        // Ensure products are loaded on initialization
        browseProductPanel.updateProductTable();
    }

    private void initializeListeners() {
        view.getInspectProductButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getInspectProductButton()) {
            handleInspectProduct();
        }
    }

    private void handleInspectProduct() {
        Product selectedProduct = browseProductPanel.getSelectedProduct();
        System.out.println(selectedProduct);
        if (selectedProduct != null) {
            // Call the CashierProductInfo view or controller
            new CashierProductInfoController(selectedProduct, mySQL);
        } else {
            JOptionPane.showMessageDialog(view, "Please select a product to inspect.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
