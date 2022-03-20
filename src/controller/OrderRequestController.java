package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import model.Order;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class OrderRequestController {

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, String> dateCol;

    @FXML
    private TableColumn<Order, Integer> tableCol;

    @FXML
    private TableColumn<Order, Integer> orderIdCol;

    @FXML
    private TableColumn<Order, String> nameCol;

    @FXML
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNum"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        setTable();
    }

    @FXML
    void doneOnAction(ActionEvent event) {
        Order order = table.getSelectionModel().getSelectedItem();

        if (order != null) {

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                conn = DatabaseTools.getConnection();
                stmt = conn.createStatement();
                String sql = "UPDATE `orders` SET `status` = 'order_recieved' WHERE orders.id = " + order.getId();
                int affectedRows = stmt.executeUpdate(sql);

                if (affectedRows > 0) {
                    AlertTools.setAlert("Success!", null, "Order Have Been Mark Done!", AlertType.INFORMATION);

                    setTable();
                } else {
                    AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
                }

                DatabaseTools.closeQueryOperation(conn, stmt, rs);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error!", null, "Please Select An Order!", AlertType.ERROR);
        }
    }

    void viewOnAction(ActionEvent event) {
        Order order = table.getSelectionModel().getSelectedItem();

        if (order != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderRequestViewPage.fxml"));

                Parent root = loader.load();

                OrderRequestViewController controller = loader.getController();

                controller.setOrder(order);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error!", null, "No Selected Order", AlertType.ERROR);
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

    private void setTable() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM `orders`, `users` WHERE DATE(order_date) = CURDATE() AND status = 'pending' AND paid = 1 AND orders.customer_id = users.id");

            while (rs.next()) {

                Order order = new Order(rs.getInt("orders.id"), rs.getString("orders.order_date"),
                        rs.getInt("orders.table_number"),
                        rs.getString("users.username"));

                table.getItems().add(order);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
