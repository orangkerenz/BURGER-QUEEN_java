package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Menu;

public class OrderRequestViewController {

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private TableColumn<Menu, Integer> quantiityCol;

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableView<Menu> table;

    private Integer orderId;

    private Integer tableNum;

    @FXML
    public void initialize() {

    }

    @FXML
    void doneOnAction(ActionEvent event) {
        Connection connection = GetConnection.getConnection();
        String sql = "UPDATE orders SET served = 1 WHERE id = " + this.orderId;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done");
        alert.setHeaderText("Done");
        alert.showAndWait();

        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderRequestPage.fxml"));
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

    void setOrderIdAndTableNum(Integer id, Integer table) {
        this.orderId = id;
        this.tableNum = table;

        orderIdText.setText("Order Id " + Integer.toString(id));
        tableText.setText("Table " + Integer.toString(tableNum));

        initTable();
    }

    void initTable() {
        quantiityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // id Menu, nama menu, times ordered,
        Connection connection = GetConnection.getConnection();
        String sql = "SELECT menus.id, menus.name, menus_has_order.quantity FROM menus_has_order, menus WHERE menus.id = menus_has_order.menus_id AND menus_has_order.orders_id = ?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Menu menu = new Menu(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("quantity"));

                table.getItems().add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
