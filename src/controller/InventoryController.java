package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.CurrentLoginUser;
import database.GetConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Ingredients;

public class InventoryController {

    @FXML
    private TableColumn<Ingredients, Double> gramCol;

    @FXML
    private TableColumn<Ingredients, String> ingredientCol;

    @FXML
    private TableView<Ingredients> table;

    @FXML
    public void initialize() {
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramCol.setCellValueFactory(new PropertyValueFactory<>("grams"));

        try {
            Connection connection = GetConnection.getConnection();
            String sql = "SELECT * FROM ingredients";

            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Ingredients ingredients = new Ingredients(rs.getString("name"), rs.getInt("id"),
                        rs.getDouble("quantity_in_grams"));

                table.getItems().add(ingredients);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {

        try {
            if (CurrentLoginUser.currentUser.getRole().equals("chef")) {
                try {
                    // ambil fxml yang dituju
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuChefPage.fxml"));
                    // load fxml`
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

            if (CurrentLoginUser.currentUser.getRole().equals("manager")) {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            CurrentLoginUser.currentUser = null;
            try {
                // ambil fxml yang dituju
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginPage.fxml"));
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
            } catch (IOException ee) {
                ee.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You are not logged in");
            alert.setContentText("Please login first");
            alert.showAndWait();
        }

    }

}
