package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.GetConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.Ingredients;
import model.Menu;

public class ListOfMenuViewController {

    @FXML
    private Text MenuName;

    @FXML
    private TableColumn<Ingredients, Double> gramCol;

    @FXML
    private TableColumn<Ingredients, String> ingredientCol;

    @FXML
    private TableView<Ingredients> table;

    private Menu menu;

    @FXML
    public void initialize() {
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramCol.setCellValueFactory(new PropertyValueFactory<>("grams"));
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

    void setMenuName(Menu menu) {
        this.menu = menu;
        MenuName.setText(menu.getName() + " Recipes");

        setTable();
    }

    private void setTable() {
        try {
            Connection connection = GetConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT ingredients.name, recipes.quantity_in_grams FROM recipes, ingredients WHERE menus_id = '"
                    + menu.getId() + "' AND recipes.ingredients_id = ingredients.id";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Ingredients ingredients = new Ingredients(menu.getId(), rs.getString("name"),
                        Double.parseDouble(rs.getBigDecimal("quantity_in_grams").toString()));
                table.getItems().add(ingredients);
            }

            connection.close();
            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
