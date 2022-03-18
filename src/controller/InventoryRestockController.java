package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InventoryRestockController {

    @FXML
    private ComboBox<String> ingredientsNameCb;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField restockQtyTf;

    @FXML
    void initialize() {
        restockQtyTf.setText("0");
        priceTf.setText("0");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = GetConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM ingredients");

            while (rs.next()) {
                ingredientsNameCb.getItems().add(rs.getString("name") + "," + rs.getInt("id"));
            }

            conn.close();
            stmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addBtn(ActionEvent event) {

        int ingredientsId = 0;
        double restockQty = 0;
        double price = 0;

        try {
            if (restockQtyTf.getText().isBlank() && priceTf.getText().isBlank()
                    && ingredientsNameCb.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Values Are Blank");
                alert.show();
                return;
            }

            ingredientsId = Integer.parseInt(ingredientsNameCb.getValue().split(",")[1]);
            restockQty = Integer.parseInt(restockQtyTf.getText());
            price = Double.parseDouble(priceTf.getText());

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill in the right value!");
            alert.show();
            return;
        }

        if (restockQty > 0 && price > 0) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = null;

            try {
                sql = "SELECT * FROM ingredients WHERE id = " + ingredientsId;
                conn = GetConnection.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    Double currentQty = rs.getDouble("quantity_in_grams");
                    Double newQty = currentQty + restockQty;

                    sql = "UPDATE ingredients SET quantity_in_grams = " + newQty + " WHERE id = "
                            + ingredientsId;
                    stmt.executeUpdate(sql);
                }

                sql = "INSERT INTO transactions(transaction_date, price, type) VALUES (NOW(), " + price + ", '"
                        + "kredit" + "')";

                stmt = conn.createStatement();
                int affectedRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

                ResultSet keys = stmt.getGeneratedKeys();

                if (keys.next()) {
                    int transactionId = keys.getInt(1);

                    sql = "INSERT INTO transactions_has_ingredients (transactions_id, ingredients_id, quantity_in_grams) VALUES ("
                            + transactionId + ", " + ingredientsId + ", " + restockQty + ")";

                    stmt.executeUpdate(sql);
                }

                conn.close();
                stmt.close();
                rs.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Information");
                alert.setContentText("Restock Success!");
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuManagerPage.fxml"));
            // load fxml
            Parent root = loader.load();
            // ambil stage/frame yang sekarang
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // buat scene baru dan tempelin root yang ingin dituju
            Scene scene = new Scene(root);
            // stage yang sekarang ambil dan tempelin scene yang baru/ingin dituju
            stage.setScene(scene);
            // show stage yang baru
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
