package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class InventoryRestockController {

    @FXML
    private ComboBox<String> ingredientsNameCb;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField restockQtyTf;

    @FXML
    void initialize() {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DatabaseTools.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ingredients");

            while (resultSet.next()) {
                ingredientsNameCb.getItems().add(resultSet.getInt("id") + "," + resultSet.getString("name"));
            }

            conn.close();
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void addBtn(ActionEvent event) {
        int ingredientsId = 0;
        int restockQty = 0;
        double price = 0;

        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            ingredientsId = Integer.parseInt(ingredientsNameCb.getValue().split(",")[0]);
            restockQty = Integer.parseInt(restockQtyTf.getText());
            price = Double.parseDouble(priceTf.getText());
        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Please Input A Valid Number!", AlertType.ERROR);

            ingredientsNameCb.getSelectionModel().clearSelection();
            JavafxTools.setTextFieldEmpty(restockQtyTf, priceTf);
            return;
        }

        if (restockQty > 0 && price > 0 && ingredientsId > 0) {
            try {
                conn = DatabaseTools.getConnection();
                statement = conn.createStatement();
                resultSet = statement
                        .executeQuery("SELECT * FROM ingredients WHERE id = '" + ingredientsId + "'");

                if (resultSet.next()) {
                    statement = conn.createStatement();
                    int result = statement
                            .executeUpdate(
                                    "UPDATE ingredients SET quantity_in_grams = quantity_in_grams + " + restockQty
                                            + " WHERE id = " + ingredientsId);

                    if (result > 0) {
                        AlertTools.setAlert("Success", null, "Restock Ingredients Successfully!",
                                AlertType.INFORMATION);

                        statement = conn.createStatement();

                        int affectedRows = statement.executeUpdate(
                                "INSERT INTO transactions (transaction_date, price, order_id, type) VALUES (NOW(), "
                                        + price + ", null" + ", 'kredit')",
                                Statement.RETURN_GENERATED_KEYS);

                        if (affectedRows > 0) {

                            resultSet = statement.getGeneratedKeys();
                            resultSet.next();
                            int transactionId = resultSet.getInt(1);

                            statement = conn.createStatement();
                            statement.executeUpdate(
                                    "INSERT INTO transactions_has_ingredients (transaction_id, ingredient_id, quantity_in_grams) VALUES ("
                                            + transactionId + ", " + ingredientsId + ", " + restockQty + ")");

                        } else {
                            AlertTools.setAlert("Error", null, "Query Error! Please Contact Support!",
                                    AlertType.ERROR);
                        }

                    } else {
                        AlertTools.setAlert("Error", null, "Restock Ingredients Failed! Please Contact Support!",
                                AlertType.ERROR);

                        ingredientsNameCb.getSelectionModel().clearSelection();
                        JavafxTools.setTextFieldEmpty(restockQtyTf, priceTf);

                        DatabaseTools.closeQueryOperation(conn, statement, resultSet);

                        return;
                    }

                } else {
                    AlertTools.setAlert("Error", null, "Ingredients Not Exist!", AlertType.ERROR);
                }

                ingredientsNameCb.getSelectionModel().clearSelection();
                JavafxTools.setTextFieldEmpty(restockQtyTf, priceTf);

                DatabaseTools.closeQueryOperation(conn, statement, resultSet);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error", null, "Please Input A Valid Number!", AlertType.ERROR);

            ingredientsNameCb.getSelectionModel().clearSelection();
            JavafxTools.setTextFieldEmpty(restockQtyTf, priceTf);
            return;
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuManagerPage.fxml");
    }

}
