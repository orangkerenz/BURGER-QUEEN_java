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

public class OrderRequestViewController {

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private TableColumn<Menu, Integer> quantiityCol;

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    public void initialize() {

    }

    @FXML
    void doneOnAction(ActionEvent event) {

    }

    void initTable() {
        quantiityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/OrderRequestPage.fxml");

    }
}
