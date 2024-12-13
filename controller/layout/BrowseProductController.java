package controller.layout;

import dataAccess.MySQL;
import model.Product;
import view.layout.BrowseProduct;

public class BrowseProductController {
    private BrowseProduct view;
    private Product selectedProduct;
    public BrowseProductController(MySQL mySQL) {
        this.view = new BrowseProduct(mySQL);
        this.selectedProduct = new Product();
    }
    public BrowseProduct getView() {
        return this.view;
    }

    public Product getSelectedProduct() {
        return this.selectedProduct;
    }

    private void setSelectedProduct() {
        return;
    }

}

