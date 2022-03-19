package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tools.DatabaseTools;
import tools.JavafxTools;
import javafx.scene.Node;

public class LeavingController {

    @FXML
    private Button leavingBtn;

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private Text informationText;

    @FXML
    public void initialize() {

    }

    @FXML
    void leavingOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/WelcomePage.fxml");
    }

}
