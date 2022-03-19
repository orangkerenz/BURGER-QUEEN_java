package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class RegisterController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void Register(ActionEvent event) {
        String username = null;
        String password = null;
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        username = usernameTf.getText();
        password = passwordTf.getText();

        try {
            conn = DatabaseTools.getConnection();

            if (!username.isBlank() && !password.isBlank()) {
                statement = conn.createStatement();
                resultSet = statement.executeQuery(
                        "SELECT * FROM users WHERE username = '" + username + "'");

                if (resultSet.next()) {

                    AlertTools.setAlert("Error!", null, "Username Exist!", AlertType.ERROR);

                    JavafxTools.setTextFieldEmpty(usernameTf, passwordTf);

                } else {

                    statement = conn.createStatement();
                    int result = statement
                            .executeUpdate("INSERT INTO users (username, password, role) VALUES ('" + username + "', '"
                                    + password + "', 'customer')");

                    if (result > 0) {
                        AlertTools.setAlert("Success!", null, "Register Success!", AlertType.INFORMATION);

                        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");
                    } else {
                        AlertTools.setAlert("Error!", null, "Register Failed!", AlertType.ERROR);

                        JavafxTools.setTextFieldEmpty(usernameTf, passwordTf);
                    }

                    DatabaseTools.closeQueryOperation(conn, statement, resultSet);
                }

            } else {
                AlertTools.setAlert("Login", "Login Failed", "Please Fill All Fields", AlertType.ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            AlertTools.setAlert("Error!", null, "Segera Hubungi Admin!", AlertType.ERROR);
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/WelcomePage.fxml");
    }
}
