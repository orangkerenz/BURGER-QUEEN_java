package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Menu;

public class ServedRequestViewController {

    @FXML
    private Text informationText;

    @FXML
    private TableColumn<Menu, Integer> menuNameCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    int orderId;

    int tableNumber;

    LinkedList<Menu> menuList = new LinkedList<Menu>();

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void doneBtn(ActionEvent event) {
        try {
            Connection connection = GetConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql = "UPDATE orders SET served = 2 WHERE id = " + orderId;
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ServedRequestPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Order has been served!");
        alert.showAndWait();

    }

    void setOrderIdAndTableNumber(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;

        informationText.setText("Order ID: " + orderId + " Table Number: " + tableNumber);

        Connection connection = GetConnection.getConnection();
        String sql = "SELECT  menus_has_order.quantity, menus.name FROM menus_has_order, menus WHERE menus_has_order.orders_id = "
                + orderId
                + " AND menus_has_order.menus_id = menus.id";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Menu menu = new Menu(resultSet.getString("name"), resultSet.getInt("quantity"));

                menuList.add(menu);
            }

            table.getItems().addAll(menuList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ServedRequestPage.fxml"));
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
