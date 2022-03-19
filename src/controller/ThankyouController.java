package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tools.JavafxTools;

public class ThankyouController {

    @FXML
    void homeOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/WelcomePage.fxml");
    }

}
