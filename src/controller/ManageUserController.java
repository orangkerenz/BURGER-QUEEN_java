package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.print.DocFlavor.STRING;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class ManageUserController {

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, String> usernameCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM users";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("role"));

                table.getItems().add(user);
            }
            DatabaseTools.closeQueryOperation(conn, statement, rs);
        } catch (Exception e) {

        }
    }

    @FXML
    void addBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/AddNewUserPage.fxml");
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

    @FXML
    void editBtn(ActionEvent event) {
        User user = table.getSelectionModel().getSelectedItem();

        if (user != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EditUserPage.fxml"));

                Parent root = loader.load();

                EditUserController controller = loader.getController();

                controller.setUser(user);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertTools.setAlert("Error!", null, "Select A User!", AlertType.ERROR);
        }
    }

}
