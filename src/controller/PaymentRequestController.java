package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Order;

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

        Connection connection = GetConnection.getConnection();
        String sql = "SELECT * FROM orders WHERE paid = 0 AND canceled = 0";
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Order order = new Order(resultSet.getInt("id"), resultSet.getString("order_date"),
                        resultSet.getInt("paid"), resultSet.getInt("users_id"), resultSet.getInt("tables_num"),
                        resultSet.getInt("canceled"), resultSet.getDouble("total_price"), resultSet.getInt("served"));
                table.getItems().add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void paidButton(ActionEvent event) {
        Order order = table.getSelectionModel().getSelectedItem();
        if (order != null) {
            Connection connection = GetConnection.getConnection();
            String sql = "UPDATE orders SET paid = 1 WHERE id = " + order.getId();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                table.getItems().remove(order);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No order selected");
            alert.setContentText("Please select an order to pay");
            alert.showAndWait();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuWaiterPage.fxml"));
            // load fxml
            Parent root = loader.load();
            // ambil stage/frame yang sekarang
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // buat scene baru dan tempelin root yang ingin dituju
            Scene scene = new Scene(root);
            // stage yang sekarang ambil dan tempelin scene yang baru/ingin dituju
            stage.setScene(scene);
            // show stage yang baru
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
