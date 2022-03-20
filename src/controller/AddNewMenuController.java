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
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

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

    private LinkedList<Ingredients> ingredientsList = new LinkedList<Ingredients>();

    @FXML
    public void initialize() {
        ingredientsNameCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramsColl.setCellValueFactory(new PropertyValueFactory<>("grams"));

        setListOfIngredientsCb();
    }

    @FXML
    void addBtn(ActionEvent event) {
        int ingredientId = 0;
        String ingredientName = null;
        double grams = 0;
        boolean isContain = false;
        try {
            ingredientId = Integer.parseInt(listOfIngredientsCb.getValue().split(",")[0]);
            ingredientName = listOfIngredientsCb.getValue().split(",")[1];
            grams = Double.parseDouble(quantityInGramsTf.getText());
        } catch (Exception e) {
            AlertTools.setAlert("Error", null, "Please Enter Correct Value", AlertType.ERROR);

            e.printStackTrace();

            JavafxTools.setTextFieldEmpty(quantityInGramsTf);

            listOfIngredientsCb.getSelectionModel().clearSelection();

            return;
        }

        Ingredients ingredient = new Ingredients(ingredientId, ingredientName, grams);

        for (Ingredients ingredients : ingredientsList) {
            if (ingredients.getIngredientsId() == ingredientId) {
                isContain = true;
                break;
            }
        }

        if (isContain) {
            AlertTools.setAlert("Error", null, "This Ingredient is already in the list", AlertType.ERROR);

            JavafxTools.setTextFieldEmpty(quantityInGramsTf);

            listOfIngredientsCb.getSelectionModel().clearSelection();

            return;
        }

        ingredientsList.add(ingredient);

        table.getItems().clear();
        table.getItems().addAll(ingredientsList);

        JavafxTools.setTextFieldEmpty(quantityInGramsTf);

        listOfIngredientsCb.getSelectionModel().clearSelection();
    }

    @FXML
    void submitBtn(ActionEvent event) {
        String menuName = null;
        double price = 0;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int affectedRows;
        int menuId;

        try {
            menuName = menuNameTf.getText();
            price = Double.parseDouble(menuPriceTf.getText());
        } catch (Exception e) {
            e.printStackTrace();

            AlertTools.setAlert("Error", null, "Please Input Right Value!", AlertType.ERROR);

            JavafxTools.setTextFieldEmpty(menuPriceTf, menuNameTf, quantityInGramsTf);

            return;
        }

        if (!menuName.isBlank() && price > 0 && !ingredientsList.isEmpty()) {

            try {
                connection = DatabaseTools.getConnection();
                statement = connection.createStatement();
                affectedRows = statement
                        .executeUpdate("INSERT INTO menus(name, price) VALUES('" + menuName + "', " + price + ")",
                                Statement.RETURN_GENERATED_KEYS);

                if (affectedRows > 0) {
                    resultSet = statement.getGeneratedKeys();

                    if (resultSet.next()) {
                        menuId = resultSet.getInt(1);

                        for (Ingredients ingredient : ingredientsList) {
                            // Should we add a affected rows?
                            statement
                                    .executeUpdate(
                                            "INSERT INTO recipes(menu_id, ingredient_id, quantity_in_grams) VALUES("
                                                    + menuId + ", " + ingredient.getIngredientsId() + ", "
                                                    + ingredient.getGrams() + ")");
                        }

                        AlertTools.setAlert("Success", null, "Menu Added Successfully", AlertType.INFORMATION);

                        ingredientsList.clear();

                        table.getItems().clear();
                    }

                } else {
                    AlertTools.setAlert("Error", null, "Error! Please Contact Support!", AlertType.ERROR);

                    JavafxTools.setTextFieldEmpty(menuPriceTf, menuNameTf, quantityInGramsTf);

                    listOfIngredientsCb.getSelectionModel().clearSelection();

                    return;
                }

            } catch (SQLException e) {
                e.printStackTrace();

                AlertTools.setAlert("Error", null, "Error! Please Contact Support!", AlertType.ERROR);
            }

        } else {
            AlertTools.setAlert("Error", null, "Please Fill In All Values!", AlertType.ERROR);
        }

        JavafxTools.setTextFieldEmpty(menuPriceTf, menuNameTf, quantityInGramsTf);

        listOfIngredientsCb.getSelectionModel().clearSelection();

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

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ListOfMenuPage.fxml");
    }
}
