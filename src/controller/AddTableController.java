package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddTableController {

    @FXML
    private TextField capacityTf;
    @FXML
    private TextField tableNumTf;

    @FXML
    void addBtn(ActionEvent event) {
        if (!capacityTf.getText().isBlank() && !tableNumTf.getText().isBlank()) {
            int tableNum = 0;
            int capacity = 0;

            try {
                tableNum = Integer.parseInt(tableNumTf.getText());
                capacity = Integer.parseInt(capacityTf.getText());
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter a valid number");
                alert.showAndWait();
                exception.printStackTrace();
                return;
            }

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                conn = GetConnection.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM tables");

                while (rs.next()) {
                    if (rs.getInt("tables_num") == tableNum) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Table number already exists");
                        alert.showAndWait();
                        return;
                    }
                }

                stmt.executeUpdate(
                        "INSERT INTO tables (tables_num, tables_capacity, avaliable) VALUES (" + tableNum + ","
                                + capacity + ", 1)");
                conn.close();
                stmt.close();
                rs.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Table added successfully");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Text Field Is Empty!");
            alert.showAndWait();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuManagerPage.fxml"));
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
