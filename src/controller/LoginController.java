package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.User;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;

public class LoginController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void login(ActionEvent event) {

        try {
            String username = null;
            String password = null;
            Connection conn = null;
            Statement statement = null;
            ResultSet resultSet = null;

            username = usernameTf.getText();
            password = passwordTf.getText();

            if (!username.isBlank() && !password.isBlank()) {

                conn = DatabaseTools.getConnection();
                statement = conn.createStatement();
                resultSet = statement.executeQuery(
                        "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'");

                if (resultSet.next()) {
                    User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("role"));

                    CurrentLoginUser.setCurrentUser(user);

                    switch (user.getRole()) {
                        case "waiter":
                            JavafxTools.changeSceneActionEvent(event, "../view/MenuWaiterPage.fxml");
                            break;

                        case "chef":
                            JavafxTools.changeSceneActionEvent(event, "../view/MenuChefPage.fxml");
                            break;

                        case "manager":
                            JavafxTools.changeSceneActionEvent(event, "../view/MenuManagerPage.fxml");
                            break;

                        case "customer":
                            JavafxTools.changeSceneActionEvent(event, "../view/TableNumberConfirmationPage.fxml");
                            break;
                    }

                } else {
                    AlertTools.setAlert("Error", null, "Username Or Password Is Invalid!", AlertType.ERROR);

                    JavafxTools.setTextFieldEmpty(usernameTf, passwordTf);
                }

                conn.close();
                statement.close();
                resultSet.close();

            } else {
                AlertTools.setAlert("Login", "Login Failed", "Please Fill All Fields", AlertType.ERROR);
            }

        } catch (Exception e) {
            AlertTools.setAlert("Error!", null, "Segera Hubungi Admin!", AlertType.ERROR);
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/WelcomePage.fxml");
    }

}
