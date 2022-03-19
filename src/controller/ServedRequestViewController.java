package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Text;

import model.Menu;

import tools.JavafxTools;

public class ServedRequestViewController {

    @FXML
    private Text informationText;

    @FXML
    private TableColumn<Menu, Integer> menuNameCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void doneBtn(ActionEvent event) {

    }

    void setOrderIdAndTableNumber(int orderId, int tableNumber) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ServedRequestPage.fxml");

    }

}
