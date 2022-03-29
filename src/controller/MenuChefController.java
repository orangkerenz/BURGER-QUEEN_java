package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.Order;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.JavafxTools;

public class MenuChefController {

    public void initialize() {
        Order.cancelOrderBiggerThanOneDay();
        System.out.println("pagi");
    }

    @FXML
    void orderRequestOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/OrderRequestPage.fxml");
    }

    @FXML
    void logoutOnAction(ActionEvent event) {
        CurrentLoginUser.setCurrentUser(null);

        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");

        AlertTools.setAlert("Logout", "Logout Success", "You Have Been Logged Out!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void listOfMenuOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/ListOfMenuPage.fxml");
    }

    @FXML
    void inventoryOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/InventoryPage.fxml");
    }

}
