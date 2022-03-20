package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.Menu;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class ListOfMenuController {

    @FXML
    private TableColumn<Menu, Integer> idCol;

    @FXML
    private TableColumn<Menu, String> menuCol;

    @FXML
    private TableColumn<Menu, String> avaliableCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        avaliableCol.setCellValueFactory(new PropertyValueFactory<>("availableString"));

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseTools.getConnection();
            statement = conn.createStatement();
            String sql = "SELECT * FROM menus";
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                table.getItems().add(new Menu(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("available"), resultSet.getInt("price")));
            }

            DatabaseTools.closeQueryOperation(conn, statement, resultSet);

        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Error", AlertType.ERROR);
        }

    }

    @FXML
    void addBtn(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/AddNewMenuPage.fxml");
    }

    @FXML
    void editBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();

        if (selectedMenu != null) {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EditMenuPage.fxml"));

                Parent root = loader.load();

                EditMenuController controller = loader.getController();

                controller.setMenu(selectedMenu);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error", null, "No Selected Menu!", AlertType.ERROR);
        }
    }

    @FXML
    void viewBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();

        if (selectedMenu != null) {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ListOfMenuViewPage.fxml"));

                Parent root = loader.load();

                ListOfMenuViewController controller = loader.getController();

                controller.setMenu(selectedMenu);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error", null, "No Selected Menu!", AlertType.ERROR);
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

}
