package controller.layout;

import model.Product;
import view.layout.BrowseProduct;

public class BrowseProductController {
    private BrowseProduct view;
    private Product selectedProduct;
    public BrowseProductController() {
        this.view = new BrowseProduct();
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

