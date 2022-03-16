package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Ingredients;

public class AddNewMenuController {

    @FXML
    private TableColumn<Ingredients, Double> gramsColl;

    @FXML
    private TableColumn<Ingredients, Double> ingredientsNameCol;

    @FXML
    private ComboBox<String> listOfIngredientsCb;

    @FXML
    private TextField menuNameTf;

    @FXML
    private TextField menuPriceTf;

    @FXML
    private TextField quantityInGramsTf;

    @FXML
    private TableView<Ingredients> table;

    LinkedList<Ingredients> ingredientsList = new LinkedList<>();

    @FXML
    public void initialize() {
        ingredientsNameCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramsColl.setCellValueFactory(new PropertyValueFactory<>("grams"));

        Connection conn = GetConnection.getConnection();
        String sql = "SELECT * FROM ingredients";

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String ingredientsName = rs.getString("name");
                listOfIngredientsCb.getItems().add(ingredientsName + "," + rs.getString("id"));
            }

            conn.close();
            statement.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void addBtn(ActionEvent event) {
        try {
            int ingredientsId = Integer.parseInt(listOfIngredientsCb.getValue().split(",")[1]);
            String ingredientsName = listOfIngredientsCb.getValue().split(",")[0];
            double grams = Double.parseDouble(quantityInGramsTf.getText());

            if (grams > 0) {
                Ingredients ingredients = new Ingredients(ingredientsName, ingredientsId, grams);

                if (ingredientsList.contains(ingredients)) {
                    ingredientsList.add(ingredients);
                    table.getItems().clear();
                    table.getItems().addAll(ingredientsList);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("You can't add the same ingredient twice");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please fill all fields");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Enter An Valid Input!");
            alert.showAndWait();
        }

    }

    @FXML
    void submitBtn(ActionEvent event) {
        double price = 0;
        String menuName = null;

        try {
            price = Double.parseDouble(menuPriceTf.getText());
            menuName = menuNameTf.getText();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Enter An Valid Input!");
            alert.showAndWait();
        }

        if (price > 0 && !menuName.isBlank()) {
            try {
                Connection conn = GetConnection.getConnection();
                String sql = "INSERT INTO menus (name, price) VALUES ('" + menuName + "', '"
                        + price + "')";
                Statement statement = conn.createStatement();
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = statement.getGeneratedKeys();
                int menuId = 0;
                if (rs.next()) {
                    menuId = rs.getInt(1);
                }

                for (Ingredients ingredients : ingredientsList) {
                    sql = "INSERT INTO recipes (menus_id, ingredients_id, quantity_in_grams) VALUES (  '" + menuId
                            + "', '"
                            + ingredients.getIngredientsId() + "', '" + ingredients.getGrams() + "')";
                    statement.executeUpdate(sql);
                }

                conn.close();
                statement.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Menu Added Successfully!");
                alert.showAndWait();

                menuNameTf.clear();
                menuPriceTf.clear();
                quantityInGramsTf.clear();
                ingredientsList.clear();
                table.getItems().clear();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Enter An Valid Input!");
            alert.showAndWait();
        }

        menuPriceTf.setText("");
        menuPriceTf.setText("");
        quantityInGramsTf.setText("");
    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ListOfMenuPage.fxml"));
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
