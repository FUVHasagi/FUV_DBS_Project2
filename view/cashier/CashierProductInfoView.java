package view.cashier;

import view.layout.ProductInformation;

import javax.swing.*;
import java.awt.*;

public class CashierProductInfoView extends JFrame {
    private ProductInformation panelProduct;

    public CashierProductInfoView(JPanel panelProduct) {
        this.setTitle("Product Information");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());

        this.panelProduct = new ProductInformation();
        initializeComponents();
    }

    private void initializeComponents() {
        this.add(panelProduct, BorderLayout.CENTER);
    }

    public JPanel getPanelProduct() {
        return panelProduct;
    }
}
