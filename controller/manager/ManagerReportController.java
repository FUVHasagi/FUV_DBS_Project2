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
    private MongoDB mongoDB;

    public ManagerReportController(Redis redis, MongoDB mongoDB) {
        this.redis = redis;
        this.mongoDB = mongoDB;
        this.view = new ManagerReportView();

        // Load report data
        loadReportData();

        // Add action listeners
        view.getBackButton().addActionListener(this);

        // Display the view
        view.setVisible(true);
    }

    private void loadReportData() {
        populateTable(view.getTableBestSellingProducts(), redis.getTopSellingProducts(), "Product", "totalQuantity");
        populateTable(view.getTableBestSellingCategories(), redis.getTopSellingCategories(), "Category", "totalQuantity");
        populateTable(view.getTableMostIncomeProducts(), redis.getTopIncomeProducts(), "Product", "totalIncome");
        populateTable(view.getTableMostIncomeCustomers(), redis.getTopIncomeCustomers(), "Customer", "totalSpent");
    }

    private void populateTable(JTable table, List<Document> data, String labelField, String valueField) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        int rank = 1;
        for (Document doc : data) {
            model.addRow(new Object[]{
                    rank++,
                    doc.get(labelField),
                    doc.get(valueField)
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBackButton()) {
            view.dispose(); // Close the report view
        }
    }
}
