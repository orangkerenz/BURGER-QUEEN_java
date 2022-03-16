package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Menu;

public class ListOfMenuController {

    @FXML
    private TableColumn<Menu, Integer> idCol;

    @FXML
    private TableColumn<Menu, String> menuCol;

    @FXML
    private TableColumn<Menu, String> avaliableCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        avaliableCol.setCellValueFactory(new PropertyValueFactory<>("avaliable"));

        try {
            Connection conn = GetConnection.getConnection();
            String sql = "SELECT * FROM menus";
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Menu menu = new Menu(rs.getInt("id"), rs.getString("name"), rs.getString("avaliable"));
                table.getItems().add(menu);
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
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddNewMenuPage.fxml"));
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
    void editBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();

        if (selectedMenu != null) {
            try {
                // ambil fxml yang dituju
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EditMenuPage.fxml"));
                // load fxml
                Parent root = loader.load();
                // controller
                EditMenuController controller = loader.getController();
                // set menu
                controller.setMenu(selectedMenu);
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

    @FXML
    void viewBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();

        if (selectedMenu != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ListOfMenuViewPage.fxml"));
                Parent root = loader.load();
                ListOfMenuViewController controller = loader.getController();
                controller.setMenuName(selectedMenu);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuChefPage.fxml"));
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
