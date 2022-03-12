package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LeavingController {

    @FXML
    private Button leavingBtn;

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private Text informationText;

    private int tableNum;

    private int orderId;

    @FXML
    public void initialize() {

        leavingBtn.setVisible(false);

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        Connection connection = GetConnection.getConnection();
                        try {
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM `orders` WHERE `id` = "
                                    + orderId);
                            if (resultSet.next()) {
                                if (resultSet.getInt("served") == 1) {
                                    informationText.setText("Order is Prepared");
                                }

                                if (resultSet.getInt("served") == 2) {
                                    informationText.setText("Order is Served");
                                    leavingBtn.setVisible(true);
                                }

                                if (resultSet.getInt("completed") == 1) {
                                    leavingBtn.setVisible(true);
                                    informationText.setText("Order is Completed");
                                    cancel();
                                }

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }, 0, 10000);

    }

    @FXML
    void leavingOnAction(ActionEvent event) {

        Connection connection = GetConnection.getConnection();

        String sql = "UPDATE tables SET avaliable = 1  WHERE tables_num = " + tableNum;

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Connection connection2 = GetConnection.getConnection();

        String sql2 = "UPDATE orders SET completed = 1 WHERE id = " + orderId;
        System.out.println(orderId);
        try {
            Statement statement = connection2.createStatement();
            statement.executeUpdate(sql2);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // go back to the main menu
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ThankyouPage.fxml"));
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

    void setOrderIdText(int orderId) {
        this.orderId = orderId;
        this.orderIdText.setText(String.valueOf(orderId));
    }

    void setTableText(int tableNumber) {
        this.tableNum = tableNumber;
        this.tableText.setText("Hi, Table - " + String.valueOf(tableNumber));
    }

}
