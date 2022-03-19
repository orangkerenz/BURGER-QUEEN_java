package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tools.JavafxTools;

public class AddTableController {

    @FXML
    private TextField capacityTf;
    @FXML
    private TextField tableNumTf;

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
