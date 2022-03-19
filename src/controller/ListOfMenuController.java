package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Menu;

import tools.JavafxTools;

public class ListOfMenuController {

    @FXML
    private TableColumn<Menu, Integer> idCol;

    @FXML
    private TableColumn<Menu, String> menuCol;

    @FXML
    private TableColumn<Menu, String> avaliableCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        avaliableCol.setCellValueFactory(new PropertyValueFactory<>("avaliable"));

    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void editBtn(ActionEvent event) {

    }

    @FXML
    void viewBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

}
