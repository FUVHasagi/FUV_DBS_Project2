package view.cashier;

import view.layout.BrowseOrderLine;
import view.layout.ProductInformation;

import javax.swing.*;
import java.awt.*;

public class CashierOrderView extends JFrame {
    private ProductInformation productInfoPanel;
    private BrowseOrderLine orderLinePanel;
    private JButton addProductButton;
    private JButton deleteProductButton;
    private JButton loadProductButton;
    private JButton clearOrderButton;
    private JButton finishAndPayButton;
    private JButton changeProductButton;
    private JButton addCustomerButton;

    public CashierOrderView() {
        setTitle("Cashier - New Order");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productInfoPanel = new ProductInformation();
        orderLinePanel = new BrowseOrderLine();
        productInfoPanel.setEditable(false);
        productInfoPanel.getFieldID().setEditable(false);

        addProductButton = new JButton("Add Product");
        deleteProductButton = new JButton("Delete Product");
        loadProductButton = new JButton("Load Product Info");
        clearOrderButton = new JButton("Clear Order");
        finishAndPayButton = new JButton("Finish & Pay");
        changeProductButton = new JButton("Change Product");
        addCustomerButton = new JButton("Add Customer");

        JPanel buttonProductPanel = new JPanel();
        buttonProductPanel.setLayout(new GridLayout(4, 2, 10, 10));
        buttonProductPanel.add(loadProductButton);
        buttonProductPanel.add(addProductButton);
        buttonProductPanel.add(changeProductButton);
        buttonProductPanel.add(deleteProductButton);
        buttonProductPanel.add(clearOrderButton);
        buttonProductPanel.add(addCustomerButton);
        buttonProductPanel.add(finishAndPayButton);

        add(productInfoPanel, BorderLayout.NORTH);
        add(orderLinePanel, BorderLayout.CENTER);
        add(buttonProductPanel, BorderLayout.SOUTH);
    }

    public ProductInformation getProductInfoPanel() {
        return productInfoPanel;
    }

    public BrowseOrderLine getOrderLinePanel() {
        return orderLinePanel;
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }

    public JButton getLoadProductButton() {
        return loadProductButton;
    }

    public JButton getClearOrderButton() {
        return clearOrderButton;
    }

    public JButton getFinishAndPayButton() {
        return finishAndPayButton;
    }

    public JButton getChangeProductButton() {
        return changeProductButton;
    }

    public JButton getAddCustomerButton() {
        return addCustomerButton;
    }
}
