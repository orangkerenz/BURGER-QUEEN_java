package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Ingredients;
import model.Menu;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class EditMenuController {

    @FXML
    private TableColumn<Ingredients, Double> gramsColl;

    @FXML
    private TableColumn<Ingredients, String> ingredientsNameCol;

    @FXML
    private ComboBox<String> listOfIngredientsCb;

    @FXML
    private ComboBox<String> menuAvalCb;

    @FXML
    private TextField menuNameTf;

    @FXML
    private TextField menuPriceTf;

    @FXML
    private TextField quantityInGramsTf;

    @FXML
    private TableView<Ingredients> table;

    private Menu menu;

    private LinkedList<Ingredients> recipesList = new LinkedList<>();

    @FXML
    public void initialize() {
        ingredientsNameCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramsColl.setCellValueFactory(new PropertyValueFactory<>("grams"));

        setListOfIngredientsCb();

    }

    @FXML
    void addBtn(ActionEvent event) {
        int ingredientId = 0;
        String ingredientsName = null;
        double quantity = 0;
        try {
            ingredientId = Integer.parseInt(listOfIngredientsCb.getValue().split(",")[0]);
            ingredientsName = listOfIngredientsCb.getValue().split(",")[1];
            quantity = Double.parseDouble(quantityInGramsTf.getText());
        } catch (Exception e) {
            e.printStackTrace();

            AlertTools.setAlert("Error", null, "Please Input Correct Amount!", AlertType.ERROR);
        }

        if (ingredientId < 0 || quantity < 0) {
            AlertTools.setAlert("Error", null, "Please Input Correct Amount!", AlertType.ERROR);
            return;
        }

        for (Ingredients ingredients : recipesList) {
            if (ingredients.getIngredientsId() == ingredientId) {
                AlertTools.setAlert("Error", null, "This Ingredient Already Exists!", AlertType.ERROR);
                return;
            }
        }

        Ingredients ingredient = new Ingredients(ingredientId, ingredientsName, quantity);

        recipesList.add(ingredient);

        table.getItems().clear();
        table.getItems().addAll(recipesList);

    }

    @FXML
    void deleteBtn(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            AlertTools.setAlert("Error", null, "Please Select a Row!", AlertType.ERROR);
            return;
        }

        recipesList.remove(index);

        table.getItems().clear();
        table.getItems().addAll(recipesList);
    }

    @FXML
    void editBtn(ActionEvent event) {
        Integer integerAvalCb = 0;
        String menuName = null;
        double price = 0;

        Connection conn = null;
        Statement statement = null;
        int affectedRows;
        try {
            integerAvalCb = (menuAvalCb.getValue().equals("Yes")) ? 1 : 0;
            menuName = menuNameTf.getText();
            price = Double.parseDouble(menuPriceTf.getText());
        } catch (Exception e) {
            e.printStackTrace();

            AlertTools.setAlert("Error", null, "Please Input Correct Value!", AlertType.ERROR);
        }

        if (integerAvalCb < 0 || price < 0 || menuName.isBlank() || recipesList.isEmpty()) {
            AlertTools.setAlert("Error", null, "Please Fill In All Fields!", AlertType.ERROR);
            return;
        }

        this.menu.setAvailableNum(integerAvalCb);
        this.menu.setName(menuName);
        this.menu.setPrice(price);

        try {
            conn = DatabaseTools.getConnection();
            statement = conn.createStatement();
            affectedRows = statement.executeUpdate("DELETE FROM recipes WHERE menu_id = " + this.menu.getId());

            if (affectedRows > 0) {
                for (Ingredients ingredients : recipesList) {
                    statement.executeUpdate(
                            "INSERT INTO recipes (menu_id, ingredient_id, quantity_in_grams) VALUES ("
                                    + this.menu.getId() + ", "
                                    + ingredients.getIngredientsId() + ", " + ingredients.getGrams() + ")");
                }

                affectedRows = statement.executeUpdate(
                        "UPDATE menus SET name = '" + this.menu.getName() + "', price = " + this.menu.getPrice()
                                + ", available = " + this.menu.getAvailableNum() + " WHERE id = "
                                + this.menu.getId());

                if (affectedRows > 0) {
                    AlertTools.setAlert("Success", null, "Menu Updated Successfully!", AlertType.INFORMATION);

                    JavafxTools.changeSceneActionEvent(event, "../view/ListOfMenuPage.fxml");
                } else {
                    AlertTools.setAlert("Error", null, "Menu Update Failed!", AlertType.ERROR);
                }

            } else {
                AlertTools.setAlert("Error", null, "Failed to Delete Recipes!", AlertType.ERROR);

                DatabaseTools.closeQueryOperation(conn, statement);

                return;
            }

            DatabaseTools.closeQueryOperation(conn, statement);

        } catch (SQLException e) {
            e.printStackTrace();

            AlertTools.setAlert("Error", null, "Error! Contact Support!", AlertType.ERROR);
        }

    }

    void setMenu(Menu menu) {
        this.menu = menu;

        setIngredientsTable();

        setAllTf();

        setMenuAvalCb();
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

    private void setAllTf() {
        menuNameTf.setText(menu.getName());
        menuPriceTf.setText(String.valueOf(menu.getPrice()));
    }

    private void setIngredientsTable() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM recipes, ingredients WHERE ingredients.id = recipes.ingredient_id AND recipes.menu_id = "
                            + menu.getId());

            while (rs.next()) {
                recipesList.add(new Ingredients(rs.getInt("ingredients.id"), rs.getString("ingredients.name"),
                        rs.getDouble("recipes.quantity_in_grams")));
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

            table.getItems().addAll(recipesList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setMenuAvalCb() {
        menuAvalCb.getItems().add("Yes");
        menuAvalCb.getItems().add("No");

        if (menu.getAvailableNum() == 1) {
            menuAvalCb.setValue("Yes");
        } else {
            menuAvalCb.setValue("No");
        }
    }

    private void setListOfIngredientsCb() {
        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM ingredients";
            ResultSet resultset = statement.executeQuery(sql);

            while (resultset.next()) {
                listOfIngredientsCb.getItems().add(resultset.getInt("id") + "," + resultset.getString("name"));
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultset);
        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Error", AlertType.ERROR);

            e.printStackTrace();
        }
    }

}
