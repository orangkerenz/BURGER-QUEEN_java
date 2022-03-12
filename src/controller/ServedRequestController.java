package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;

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

        Connection connection = GetConnection.getConnection();
        String sql = "SELECT * FROM orders WHERE served = 1 AND canceled = 0 AND paid = 1 AND completed = 0";
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
    void doneBtn(ActionEvent event) {
        Order order = table.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select an order");
            alert.showAndWait();
            return;
        }
        Connection connection = GetConnection.getConnection();
        String sql = "UPDATE orders SET served = 2 WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.executeUpdate();
            table.getItems().remove(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewBtn(ActionEvent event) {

    }

}
