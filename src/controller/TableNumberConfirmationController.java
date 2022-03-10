package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import model.Table;
import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class TableNumberConfirmationController {

    @FXML
    private ComboBox<String> tableNumberCb;

    @FXML
    public void initialize() {
        ArrayList<Table> tableNumbers = new ArrayList<Table>();

        Connection conn = GetConnection.getConnection();
        String sql = "SELECT * FROM tables WHERE avaliable = 1";
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Table table = new Table(resultSet.getInt("tables_num"),
                        resultSet.getInt("tables_capacity"), resultSet.getInt("avaliable"));

                tableNumbers.add(table);
            }

            conn.close();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Table table : tableNumbers) {
            tableNumberCb.getItems()
                    .add("Table Number : " + Integer.toString(table.getTablesNum()) + " ( Capacity : "
                            + table.getTableCapacity() + ")");

        }
    }

    @FXML
    void confirmButton(ActionEvent event) {
        String tableNumber = tableNumberCb.getValue();
        String tableNumberString = tableNumber.substring(tableNumber.indexOf(":") + 2, tableNumber.indexOf("("));

        Connection conn = GetConnection.getConnection();
        String sql = "UPDATE tables SET avaliable = 0 WHERE tables_num = " + tableNumberString;
        Statement statement = null;

        try {
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SelectMenuPage.fxml"));
            // load fxml
            Parent root = loader.load();
            // pangil controller yang dituju
            SelectMenuController selectMenuController = loader.getController();
            // pangil method yang set table number
            selectMenuController.setTableNum(tableNumberString);
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
