package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Ingredients;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;

public class InventoryController {

    @FXML
    private TableColumn<Ingredients, Integer> idCol;

    @FXML
    private TableColumn<Ingredients, Double> gramCol;

    @FXML
    private TableColumn<Ingredients, String> ingredientsCol;

    @FXML
    private TableView<Ingredients> table;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsId"));
        ingredientsCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramCol.setCellValueFactory(new PropertyValueFactory<>("grams"));

        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM ingredients";
            ResultSet resultset = statement.executeQuery(sql);

            while (resultset.next()) {
                table.getItems().add(new Ingredients(resultset.getInt("id"), resultset.getString("name"),
                        resultset.getDouble("quantity_in_grams")));
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultset);
        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Error", AlertType.ERROR);

            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {

        try {
            if (CurrentLoginUser.getRole().equals("chef")) {

                JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");

            }

            if (CurrentLoginUser.getRole().equals("manager")) {

                JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");

            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            JavafxTools.changeSceneMouseEvent(event, "../view/LoginPage.fxml");

            AlertTools.setAlert("Error",
                    "You are not logged in", "Please login first!", AlertType.INFORMATION);

        }

    }

}
