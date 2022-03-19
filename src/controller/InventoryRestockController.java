package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tools.JavafxTools;

public class InventoryRestockController {

    @FXML
    private ComboBox<String> ingredientsNameCb;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField restockQtyTf;

    @FXML
    void initialize() {

    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
