package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Ingredients;
import model.Menu;
import tools.DatabaseTools;
import tools.JavafxTools;

public class ListOfMenuViewController {

    @FXML
    private Text MenuName;

    @FXML
    private TableColumn<Ingredients, Double> gramCol;

    @FXML
    private TableColumn<Ingredients, String> ingredientCol;

    @FXML
    private TableView<Ingredients> table;

    private Menu targetedMenu;

    @FXML
    public void initialize() {
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramCol.setCellValueFactory(new PropertyValueFactory<>("grams"));
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ListOfMenuPage.fxml");
    }

    void setMenu(Menu menu) {
        this.targetedMenu = menu;

        setMenuNameText();

        setTableRecipes();
    }

    private void setTableRecipes() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT ingredients.name, recipes.quantity_in_grams FROM recipes, ingredients WHERE recipes.ingredient_id = ingredients.id AND recipes.menu_id = "
                            + targetedMenu.getId());

            while (rs.next()) {
                table.getItems()
                        .add(new Ingredients(rs.getString("name"), rs.getDouble("quantity_in_grams")));
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setMenuNameText() {
        MenuName.setText(targetedMenu.getName());
    }

}
