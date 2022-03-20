package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.JavafxTools;

public class MenuManagerController {

    @FXML
    void bookkeepingBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/BookkeepingPage.fxml");
    }

    @FXML
    void inventoryBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/InventoryPage.fxml");
        // System.out.println("halo");
    }

    @FXML
    void inventoryRestockBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/InventoryRestockPage.fxml");
    }

    @FXML
    void logoutOnAction(ActionEvent event) {
        CurrentLoginUser.setCurrentUser(null);

        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");

        AlertTools.setAlert("Logout", "Logout Success", "You Have Been Logged Out!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void newIngredientsBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/AddNewIngredientsPage.fxml");
    }

    @FXML
    void addTableBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/AddTablePage.fxml");
    }

}
