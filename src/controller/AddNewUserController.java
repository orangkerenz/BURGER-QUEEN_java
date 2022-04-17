package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class AddNewUserController {

    @FXML
    private TextField passwordTf;

    @FXML
    private ComboBox<String> roleCb;

    @FXML
    private TextField usernameTf;

    public void initialize() {
        LinkedList<String> ls = new LinkedList<>();
        ls.add("customer");
        ls.add("waiter");
        ls.add("chef");
        ls.add("manager");

        roleCb.getItems().addAll(ls);
    }

    @FXML
    void addBtn(ActionEvent event) {

        if (usernameTf.getText().isBlank() || passwordTf.getText().isBlank() || roleCb.getValue() == null) {
            AlertTools.setAlert("Error!", null, "One or more of your text field is blank!", AlertType.ERROR);
            usernameTf.setText("");
            passwordTf.setText("");
            return;
        }

        try {

            Connection conn = DatabaseTools.getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO users(username, password, role) VALUES(?, ?, ?)");
            stmt.setString(1, usernameTf.getText());
            stmt.setString(2, passwordTf.getText());
            stmt.setString(3, roleCb.getValue());

            stmt.executeUpdate();

            AlertTools.setAlert("Success!", null, "New Users Has Been Added!", AlertType.INFORMATION);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        usernameTf.setText("");
        passwordTf.setText("");

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ManageUserPage.fxml");
    }

}
