package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import model.Menu;

public class ServedRequestViewController {

    @FXML
    private Text informationText;

    @FXML
    private TableColumn<Menu, Integer> menuNameCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    int orderId;

    int tableNumber;

    @FXML
    void doneBtn(ActionEvent event) {

    }

    void setOrderIdAndTableNumber(int orderId, int tableNumber) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
    }

}
