package controller.cashier;

import dataAccess.MySQL;
import model.Product;
import view.cashier.CashierProductInfoView;
import view.layout.ProductInformation;

public class CashierProductInfoController {
    private CashierProductInfoView view;
    private Product product;
    private MySQL mySQL;

    public CashierProductInfoController(Product product, MySQL mySQL) {
        this.product = product;
        this.mySQL = mySQL;

        // Create ProductInformation panel and populate it
        ProductInformation productInfoPanel = new ProductInformation();
        productInfoPanel.setProduct(product);
        productInfoPanel.setEditable(false); // Ensure the panel is read-only for the cashier

        // Initialize the view
        this.view = new CashierProductInfoView(productInfoPanel);
        this.view.setVisible(true);
    }
}
