package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LeavingController {

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    void leavingOnAction(ActionEvent event) {
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
        this.orderIdText.setText(String.valueOf(orderId));
    }

    void setTableText(int tableNumber) {
        this.tableText.setText("Hi, Table - " + String.valueOf(tableNumber));
    }

}
