package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    }

    @FXML
    void backBtn(MouseEvent event) {

    }

}
