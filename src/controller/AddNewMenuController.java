package controller;

import java.util.LinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Ingredients;
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

    @FXML
    public void initialize() {
        ingredientsNameCol.setCellValueFactory(new PropertyValueFactory<>("ingredientsName"));
        gramsColl.setCellValueFactory(new PropertyValueFactory<>("grams"));

    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void submitBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

}
