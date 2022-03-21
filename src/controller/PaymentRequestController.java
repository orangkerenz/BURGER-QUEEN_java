package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Order;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class PaymentRequestController {

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, String> orderIdCol;

    @FXML
    private TableColumn<Order, Integer> tableCol;

    @FXML
    private TableColumn<Order, Double> totalCol;

    @FXML
    private TableColumn<Order, String> DateTimeCol;

    @FXML
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNum"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        DateTimeCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        setTable();
    }

    private void setTable() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM `orders` WHERE paid = 0 AND status != 'canceled' AND DATE(order_date) = CURDATE() ");

            while (rs.next()) {
                Order order = new Order(rs.getInt("orders.id"), rs.getString("orders.order_date"),
                        rs.getInt("orders.table_number"),
                        rs.getDouble("orders.total_price"));

                table.getItems().add(order);

            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void paidButton(ActionEvent event) {
        Order order = table.getSelectionModel().getSelectedItem();
        Connection conn = null;
        Statement stmt = null;
        int affectedRows1;
        int affectedRows2;

        if (order != null) {
            try {
                conn = DatabaseTools.getConnection();
                stmt = conn.createStatement();
                affectedRows1 = stmt.executeUpdate("UPDATE `orders` SET paid = 1 WHERE id = " + order.getId());
                affectedRows2 = addTransaction(order);

                if (affectedRows1 > 0 && affectedRows2 > 0) {
                    AlertTools.setAlert("Success", null, "Order Has Been Mark Paid!", Alert.AlertType.INFORMATION);

                    table.getItems().remove(order);

                } else {
                    AlertTools.setAlert("Error", null, "Contact Support!", AlertType.ERROR);
                }

                DatabaseTools.closeQueryOperation(conn, stmt);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            AlertTools.setAlert("Error", null, "Please Select An Order!", AlertType.ERROR);
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuWaiterPage.fxml");
    }

    private int addTransaction(Order order) {
        Connection conn = null;
        Statement stmt = null;

        try {
            System.out.println("halo");
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            String sql = "INSERT INTO `transactions` (transaction_date, price, order_id, type) VALUES (NOW(), '"
                    + order.getTotalPrice() + "', '" + order.getId() + "', 'debit');";
            int affectedRows = stmt.executeUpdate(sql);

            DatabaseTools.closeQueryOperation(conn, stmt);

            return affectedRows;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

}
