package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tools.JavafxTools;

public class RegisterController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void Register(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {

        JavafxTools.changeSceneMouseEvent(event, "../view/WelcomePage.fxml");
    }
}
