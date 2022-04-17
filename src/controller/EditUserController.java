package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.User;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class EditUserController {

    @FXML
    private TextField passwordTf;

    @FXML
    private ComboBox<String> roleCb;

    @FXML
    private TextField usernameTf;

    private User user;

    public void initialize() {
        LinkedList<String> ls = new LinkedList<>();
        ls.add("customer");
        ls.add("waiter");
        ls.add("chef");
        ls.add("manager");

        roleCb.getItems().addAll(ls);
    }

    @FXML
    void editBtn(ActionEvent event) {

        if (usernameTf.getText().isBlank() || passwordTf.getText().isBlank() || roleCb.getValue() == null) {
            AlertTools.setAlert("Error!", null, "One or more of your text field is blank!", AlertType.ERROR);
            usernameTf.setText("");
            passwordTf.setText("");
            return;
        }

        try {
            if (!user.getUsername().equals(usernameTf.getText())) {
                Connection conn = DatabaseTools.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + usernameTf.getText() + "'");

                if (rs.next()) {
                    AlertTools.setAlert("Error!", null, "Username already exists!", AlertType.ERROR);

                    usernameTf.setText(user.getUsername());
                    passwordTf.setText(user.getPassword());
                    switch (user.getRole()) {
                        case "customer":
                            roleCb.getSelectionModel().select(0);
                            break;
                        case "waiter":
                            roleCb.getSelectionModel().select(1);
                            break;
                        case "chef":
                            roleCb.getSelectionModel().select(2);
                            break;
                        case "manager":
                            roleCb.getSelectionModel().select(3);
                            break;
                    }

                    return;
                } else {
                    Connection conn2 = DatabaseTools.getConnection();
                    PreparedStatement stmt2 = conn2
                            .prepareStatement("UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?");
                    stmt2.setString(1, usernameTf.getText());
                    stmt2.setString(2, passwordTf.getText());
                    stmt2.setString(3, roleCb.getValue());
                    stmt2.setInt(4, user.getId());

                    stmt2.executeUpdate();

                    AlertTools.setAlert("Success!", null, "Users Have Been Edited", AlertType.INFORMATION);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JavafxTools.changeSceneActionEvent(event, "../view/ManageUserPage.fxml");
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ManageUserPage.fxml");
    }

    public void setUser(User user) {
        this.user = user;
        usernameTf.setText(user.getUsername());
        passwordTf.setText(user.getPassword());
        switch (user.getRole()) {
            case "customer":
                roleCb.getSelectionModel().select(0);
                break;
            case "waiter":
                roleCb.getSelectionModel().select(1);
                break;
            case "chef":
                roleCb.getSelectionModel().select(2);
                break;
            case "manager":
                roleCb.getSelectionModel().select(3);
                break;
        }
    }

}
