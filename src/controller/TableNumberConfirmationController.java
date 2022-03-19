package controller;

import tools.JavafxTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

public class TableNumberConfirmationController {

    @FXML
    private ComboBox<String> tableNumberCb;

    @FXML
    public void initialize() {

    }

    @FXML
    void confirmButton(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/WelcomePage.fxml");
    }

}
