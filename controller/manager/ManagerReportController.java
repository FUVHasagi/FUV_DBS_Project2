package controller.manager;

import dataAccess.MongoDB;
import dataAccess.Redis;
import org.bson.Document;
import view.manager.ManagerReportView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagerReportController implements ActionListener {
    private ManagerReportView view;
    private Redis redis;

    public ManagerReportController(Redis redis, MongoDB mongoDB) {
        this.redis = redis;
        this.view = new ManagerReportView();

        loadReportData();
        view.getRefreshButton().addActionListener(this);
        view.setVisible(true);
    }

    private void loadReportData() {
        populateTable(view.getTableBestSellingProducts(), redis.getTopSellingProducts(), "productID", "totalQuantity");
        populateTable(view.getTableBestSellingCategories(), redis.getTopSellingCategories(), "category", "totalQuantity");
        populateTable(view.getTableMostIncomeProducts(), redis.getTopIncomeProducts(), "productID", "totalIncome");
        populateTable(view.getTableMostIncomeCustomers(), redis.getTopIncomeCustomers(), "customerID", "totalSpent");
    }

    private void populateTable(JTable table, List<Document> data, String labelField, String valueField) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        int rank = 1;
        for (Document doc : data) {
            model.addRow(new Object[]{rank++, doc.getString(labelField), doc.get(valueField)});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getRefreshButton()) {
            redis.reloadCache("top_selling_products");
            redis.reloadCache("top_selling_categories");
            redis.reloadCache("top_income_products");
            redis.reloadCache("top_income_customers");
            loadReportData();
            JOptionPane.showMessageDialog(view, "Report data refreshed.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
