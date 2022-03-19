package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import tools.AlertTools;
import tools.JavafxTools;

public class MenuWaiterController {

    @FXML
    void logoutOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");

        AlertTools.setAlert("Logout", "Logout Success", "You Have Been Logged Out!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void paymentRequestOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/PaymentRequestPage.fxml");
    }

    @FXML
    void servedRequestOnAction(ActionEvent event) {

        JavafxTools.changeSceneActionEvent(event, "../view/ServedRequestPage.fxml");

    }

}
