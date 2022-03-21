package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Order;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class ServedRequestController {

    @FXML
    private TableColumn<Order, Integer> orderIdCol;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> tableCol;

    @FXML
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNum"));

        setTable();
    }

    @FXML
    void doneBtn(ActionEvent event) {
        Order selectedOrder = table.getSelectionModel().getSelectedItem();
        Connection conn = null;
        Statement stmt = null;
        int affectedRows;
        if (selectedOrder != null) {

            try {

                conn = DatabaseTools.getConnection();
                stmt = conn.createStatement();
                affectedRows = stmt
                        .executeUpdate("UPDATE orders SET status = 'served' WHERE id = " + selectedOrder.getId());

                if (affectedRows > 0) {
                    AlertTools.setAlert("Succes", null, "Order Is Mark Served!", AlertType.INFORMATION);

                    setTable();
                } else {
                    AlertTools.setAlert("Error", null, "Error Contact Support!", AlertType.ERROR);
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
    void viewBtn(ActionEvent event) {
        Order selectedOrder = table.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ServedRequestViewPage.fxml"));

                Parent root = loader.load();

                ServedRequestViewController controller = loader.getController();

                controller.setOrder(selectedOrder);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (IOException e) {
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

    public void setTable() {
        table.getItems().clear();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM `orders` WHERE paid = 1 AND status = 'ready' AND DATE(order_date) = CURDATE() AND paid = 1");

            while (rs.next()) {
                Order order = new Order(rs.getInt("orders.id"),
                        rs.getInt("orders.table_number"));
                table.getItems().add(order);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
