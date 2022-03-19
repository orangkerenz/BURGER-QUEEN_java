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

public class AddTableController {

    @FXML
    private TextField capacityTf;
    @FXML
    private TextField tableNumTf;

    @FXML
    void addBtn(ActionEvent event) {
        int capacity = 0;
        int tableNum = 0;

        try {
            capacity = Integer.parseInt(capacityTf.getText());
            tableNum = Integer.parseInt(tableNumTf.getText());

        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Please Enter A Number!", AlertType.ERROR);
            return;
        }

        if (capacity < 1 || tableNum < 1) {
            AlertTools.setAlert("Error", null, "Enter An Valid Number", AlertType.ERROR);

            return;
        } else {

            try {
                Connection conn = DatabaseTools.getConnection();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM tables WHERE table_number = " + tableNum);

                if (resultSet.next()) {
                    AlertTools.setAlert("Error", null, "Table Number Is Exist!", AlertType.ERROR);

                    JavafxTools.setTextFieldEmpty(tableNumTf, capacityTf);

                    return;
                } else {
                    statement = conn.createStatement();
                    int result = statement.executeUpdate(
                            "INSERT INTO tables (table_number, table_capacity) VALUES (" + tableNum + ", "
                                    + capacity
                                    + ")");

                    if (result > 0) {
                        AlertTools.setAlert("Success", null, "New Table Number Is Succesfully Input!",
                                AlertType.INFORMATION);
                    } else {
                        AlertTools.setAlert("Error", null, "Query Error! Please Contact Support!", AlertType.ERROR);
                    }

                    JavafxTools.setTextFieldEmpty(tableNumTf, capacityTf);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
