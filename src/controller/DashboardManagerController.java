package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardManagerController {

    @FXML
    private DatePicker dateEnd;

    @FXML
    private DatePicker dateStart;

    @FXML
    private Text totalDebit;

    @FXML
    private Text totalKredit;

    @FXML
    private Text totalOrder;

    @FXML
    private Text totalRevenue;

    public void initialize() {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = " SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'kredit'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalKredit.setText("$ " + rs.getString("price"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'debit'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalDebit.setText("$ " + rs.getString("price"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'debit' UNION SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'kredit'";

            ResultSet rs = statement.executeQuery(sql);

            double revenue = 0;

            if (rs.next()) {
                revenue += rs.getDouble("price");
            }

            if (rs.next()) {
                revenue -= rs.getDouble("price");
            }

            totalRevenue.setText("$ " + revenue);

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT COUNT(*) FROM orders WHERE paid = '1'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalOrder.setText(rs.getString("COUNT(*)"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

    @FXML
    void search(ActionEvent event) {
        if (dateEnd.getValue() == null || dateStart.getValue() == null) {
            AlertTools.setAlert("Error", "Please select a date range", "Please select a date range", AlertType.ERROR);

            return;
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = " SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'kredit' AND DATE(transaction_date) BETWEEN '"
                    + dateStart.getValue() + "' AND '" + dateEnd.getValue() + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalKredit.setText("$ " + rs.getString("price"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'debit' AND DATE(transaction_date) BETWEEN '"
                    + dateStart.getValue() + "' AND '" + dateEnd.getValue() + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalDebit.setText("$ " + rs.getString("price"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'debit' AND DATE(transaction_date) BETWEEN '"
                    + dateStart.getValue() + "' AND '" + dateEnd.getValue()
                    + "' UNION SELECT SUM(transactions.price) AS price FROM transactions WHERE type = 'kredit' AND DATE(transaction_date) BETWEEN '"
                    + dateStart.getValue() + "' AND '" + dateEnd.getValue() + "'";

            ResultSet rs = statement.executeQuery(sql);

            double revenue = 0;

            if (rs.next()) {
                revenue += rs.getDouble("price");
            }

            if (rs.next()) {
                revenue -= rs.getDouble("price");
            }

            totalRevenue.setText("$ " + revenue);

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT COUNT(*) FROM orders WHERE paid = '1' AND DATE(order_date) BETWEEN '"
                    + dateStart.getValue() + "' AND '" + dateEnd.getValue() + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                totalOrder.setText(rs.getString("COUNT(*)"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
