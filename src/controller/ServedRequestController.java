package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Order;

import tools.JavafxTools;

public class ServedRequestController {

    @FXML
    private TableColumn<Order, Integer> orderIdCol;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> tableCol;

    @FXML
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNum"));
    }

    @FXML
    void doneBtn(ActionEvent event) {

    }

    @FXML
    void viewBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuWaiterPage.fxml");
    }

}
