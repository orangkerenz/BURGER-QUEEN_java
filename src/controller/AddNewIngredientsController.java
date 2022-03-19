package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class AddNewIngredientsController {

    @FXML
    private TextField ingredientsNameTf;

    @FXML
    void addBtn(ActionEvent event) {

        String ingredientsName = ingredientsNameTf.getText();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        if (ingredientsName.isBlank()) {
            AlertTools.setAlert("Error", null, "Please Input A Valid Name!", AlertType.ERROR);
            return;
        }

        try {
            conn = DatabaseTools.getConnection();
            statement = conn.createStatement();
            resultSet = statement
                    .executeQuery("SELECT * FROM ingredients WHERE name = '" + ingredientsName + "'");

            if (resultSet.next()) {
                AlertTools.setAlert("Error", null, "Ingredients Name Is Exist!", AlertType.ERROR);

                JavafxTools.setTextFieldEmpty(ingredientsNameTf);

                return;
            } else {

                statement = conn.createStatement();
                int result = statement
                        .executeUpdate("INSERT INTO ingredients (name) VALUES ('" + ingredientsName + "')");

                if (result > 0) {
                    AlertTools.setAlert("Success", null, "Add Ingredients Successfully!", AlertType.INFORMATION);
                } else {
                    AlertTools.setAlert("Error", null, "Add Ingredients Failed!", AlertType.ERROR);
                }

                JavafxTools.setTextFieldEmpty(ingredientsNameTf);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
