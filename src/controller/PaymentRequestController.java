package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Order;
import tools.JavafxTools;

public class PaymentRequestController {

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, String> orderIdCol;

    @FXML
    private TableColumn<Order, Integer> tableCol;

    @FXML
    private TableColumn<Order, Double> totalCol;

    @FXML
    private TableColumn<Order, String> DateTimeCol;

    @FXML
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNum"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        DateTimeCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    }

    @FXML
    void paidButton(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuWaiterPage.fxml");
    }

}
