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
import model.Transaction;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class BookkeepingController {

    @FXML
    private TableColumn<Transaction, Double> amountCol;

    @FXML
    private TableColumn<Transaction, String> dateCol;

    @FXML
    private TableColumn<Transaction, Integer> idCol;

    @FXML
    private TableView<Transaction> table;

    @FXML
    private TableColumn<Transaction, String> typeCol;

    @FXML
    void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseTools.getConnection();
            statement = conn.createStatement();
            String sql = "SELECT * FROM transactions";
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                table.getItems().add(new Transaction(resultSet.getInt("id"), resultSet.getString("type"),
                        resultSet.getDouble("price"), resultSet.getString("transaction_date")));
            }

            DatabaseTools.closeQueryOperation(conn, statement, resultSet);

        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Error", AlertType.ERROR);

            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
