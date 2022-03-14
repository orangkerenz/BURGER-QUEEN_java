package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class WelcomeController {

    @FXML
    void loginBtn(ActionEvent event) throws IOException {
        // ambil fxml yang dituju
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginPage.fxml"));
        // ambil stage/frame yang sekarang
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // buat scene baru dan tempelin root yang ingin dituju
        Scene scene = new Scene(root);
        // stage yang sekarang ambil dan tempelin scene yang baru/ingin dituju
        stage.setScene(scene);
        // show stage yang baru
        stage.show();
    }

    @FXML
    void registerBtn(ActionEvent event) throws IOException {
        // ambil fxml yang dituju
        Parent root = FXMLLoader.load(getClass().getResource("../view/RegisterPage.fxml"));
        // ambil stage/frame yang sekarang
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // buat scene baru dan tempelin root yang ingin dituju
        Scene scene = new Scene(root);
        // stage yang sekarang ambil dan tempelin scene yang baru/ingin dituju
        stage.setScene(scene);
        // show stage yang baru
        stage.show();
    }
}
