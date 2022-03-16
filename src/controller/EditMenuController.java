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
import model.Menu;

public class EditMenuController {

    @FXML
    private TableColumn<Ingredients, Double> gramsColl;

    @FXML
    private TableColumn<Ingredients, String> ingredientsNameCol;

    @FXML
    private ComboBox<String> listOfIngredientsCb;

    @FXML
    private TextField menuAvalTf;

    @FXML
    private TextField menuNameTf;

    @FXML
    private TextField menuPriceTf;

    @FXML
    private TextField quantityInGramsTf;

    @FXML
    private TableView<Ingredients> table;

    LinkedList<Ingredients> ingredientsList = new LinkedList<>();

    Menu menu = null;

    @FXML
    public void initialize() {
        ingredientsNameCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramsColl.setCellValueFactory(new PropertyValueFactory<>("grams"));

        Connection conn = GetConnection.getConnection();
        String sql = null;
        Statement statement = null;
        ResultSet rs = null;

        conn = GetConnection.getConnection();
        sql = "SELECT * FROM ingredients";

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

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

                if (!ingredientsList.contains(ingredients)) {
                    ingredientsList.add(ingredients);
                    table.getItems().clear();
                    table.getItems().addAll(ingredientsList);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("You Cannot Input The Same Ingredients");
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

    @FXML
    void deleteBtn(ActionEvent event) {
        Ingredients ingredients = table.getSelectionModel().getSelectedItem();

        if (ingredients != null) {
            ingredientsList.remove(ingredients);
            table.getItems().clear();
            table.getItems().addAll(ingredientsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Select An Item!");
            alert.showAndWait();
        }
    }

    @FXML
    void editBtn(ActionEvent event) {
        // hapus semua di table recipe dengan menu id yang sama
        Connection conn = GetConnection.getConnection();
        String sql = null;

        // jika tidak kosong insert ke table recipe
        if (ingredientsList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Input Ingredients!");
            alert.showAndWait();
        } else {
            // check apakah kosong
            if (menu != null) {
                sql = "DELETE FROM recipes WHERE menus_id = " + menu.getId();
                try {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(sql);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // insert ke table recipe yang baru
            for (Ingredients ingredients : ingredientsList) {
                sql = "INSERT INTO recipes (menus_id, ingredients_id, quantity_in_grams) VALUES (" + menu.getId() + ", "
                        + ingredients.getIngredientsId() + ", " + ingredients.getGrams() + ")";
                try {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(sql);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {

            // check apakah nama,menu aval dan menu price itu sama dengan yang lama
            String menuName = menuNameTf.getText();
            String menuAval = menuAvalTf.getText();
            Double menuPrice = Double.parseDouble(menuPriceTf.getText());

            if (menuName.isBlank() && menuAval.isBlank() && menuPrice < 0
                    && (menuAval.equals("1") || menuAval.equals("0"))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please Fill Valid Value!");
                alert.showAndWait();
            } else {
                if (menu != null) {
                    if (menuName.equals(menu.getName()) && menuAval.equals(menu.getAvaliable())
                            && menuPrice == menu.getPrice()) {

                    } else {
                        // update menu yang lama
                        sql = "UPDATE menus SET name = '" + menuName + "', avaliable = " + menuAval + ", price = "
                                + menuPrice + " WHERE id = " + menu.getId();
                        try {
                            Statement statement = conn.createStatement();
                            statement.executeUpdate(sql);
                            statement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Information");
                    alert.setContentText("Menu Successfully Edited!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please Fill Valid Value!");
            alert.showAndWait();
        }

    }

    void setMenu(Menu menu) {
        this.menu = menu;

        setTableValue();
    }

    private void setTableValue() {
        try {
            Connection connection = GetConnection.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT ingredients.name, recipes.quantity_in_grams, ingredients.id FROM recipes, ingredients WHERE menus_id = '"
                    + menu.getId() + "' AND recipes.ingredients_id = ingredients.id";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Ingredients ingredients = new Ingredients(menu.getId(), rs.getString("name"),
                        Double.parseDouble(rs.getBigDecimal("quantity_in_grams").toString()), rs.getInt("id"));
                ingredientsList.add(ingredients);
            }

            connection.close();
            statement.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.getItems().clear();
        table.getItems().addAll(ingredientsList);

        Connection conn = GetConnection.getConnection();
        String sql = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            sql = "SELECT * FROM menus WHERE id = " + menu.getId();
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                this.menu = new Menu(rs.getInt("id"), rs.getString("name"), rs.getString("avaliable"),
                        rs.getDouble("price"));

                menuNameTf.setText(menu.getName());
                menuAvalTf.setText(menu.getAvaliable());
                menuPriceTf.setText(String.valueOf(menu.getPrice()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
