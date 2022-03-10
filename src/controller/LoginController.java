package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.User;

public class LoginController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void login(ActionEvent event) {
        if (usernameTf.getText().isBlank() || passwordTf.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username and Password cannot be empty");
            alert.showAndWait();
            return;
        } else if (usernameTf.getText() == null || passwordTf.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username and Password cannot be empty");
            alert.showAndWait();
            return;
        }

        Connection connection = GetConnection.getConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usernameTf.getText());
            preparedStatement.setString(2, passwordTf.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getString("role"));

                // check the role
                switch (user.getRole()) {
                    case "waiter":
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
                        break;
                    case "chef":
                        try {
                            // ambil fxml yang dituju
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuChefPage.fxml"));
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
                        break;
                    case "manager":
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
                        break;
                }

                preparedStatement.close();
                connection.close();
                resultSet.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
