package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tools.JavafxTools;

public class WelcomeController {

    @FXML
    void loginBtn(ActionEvent event) throws IOException {
        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");
    }

    @FXML
    void registerBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/RegisterPage.fxml");
    }
}
