package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Menu;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;
import javafx.scene.Node;

public class OrderConfirmationController {

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableColumn<Menu, Double> priceCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    private Text totalText;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button doneBtn;

    @FXML
    private Text informationText;

    @FXML
    private Text orderIdText;

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));

    }

    @FXML
    void cancelOnAction(ActionEvent event) {

    }

    @FXML
    void doneOnAction(ActionEvent event) {

    }

    @FXML
    void logoutBtn(ActionEvent event) {
        CurrentLoginUser.setCurrentUser(null);
        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");
    }

    void newOrder() {

    }

    void newTransaction() {

    }

}
