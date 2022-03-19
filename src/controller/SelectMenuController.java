package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Menu;
import tools.GetConnection;
import tools.JavafxTools;
import javafx.scene.Node;

public class SelectMenuController {

    @FXML
    private TableColumn<Menu, Integer> idCol;

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableColumn<Menu, Double> priceCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void deleteteBtn(ActionEvent event) {

    }

    @FXML
    void doneBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/TableNumberConfirmationPage.fxml");
    }

}
