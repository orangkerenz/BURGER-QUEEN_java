package controller;

import java.io.IOException;
import java.sql.Connection;

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

public class AddNewIngredientsController {

    @FXML
    private TextField ingredientsNameTf;

    @FXML
    void addBtn(ActionEvent event) {
        if (!ingredientsNameTf.getText().isBlank()) {

            Connection connection = null;
            try {
                connection = GetConnection.getConnection();
                connection.createStatement().executeUpdate(
                        "INSERT INTO ingredients (name) VALUES ('" + ingredientsNameTf.getText() + "')");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add New Ingredients");
                alert.setHeaderText("Add New Ingredients Success");
                alert.setContentText("New Ingredients has been added");
                alert.showAndWait();

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in the ingredients name");
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
