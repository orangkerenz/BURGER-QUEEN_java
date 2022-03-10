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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Menu;
import javafx.scene.Node;

public class SelectMenuController {

    @FXML
    private TableColumn<Menu, Integer> idCol;

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableColumn<Menu, Double> priceCol;

    @FXML
    private TableView<Menu> table;

    private LinkedList<Menu> menuList = new LinkedList<Menu>();

    private LinkedList<Menu> listOfOrder = new LinkedList<Menu>();

    private Integer tableNum;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        Connection connection = GetConnection.getConnection();
        String sql = "SELECT * FROM menus";
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Menu menu = new Menu(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("price"));

                menuList.add(menu);
            }

            connection.close();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Menu menu : menuList) {
            table.getItems().add(menu);
        }

    }

    @FXML
    void addBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();
        boolean menuExist = false;
        if (selectedMenu != null) {
            // check if the selectedMenu is already in the listOfOrder
            for (Menu menu : listOfOrder) {
                if (menu.getId() == selectedMenu.getId()) {
                    // set a +1 timesOrdered
                    menu.setTimesOrdered(menu.getTimesOrdered() + 1);
                    menuExist = true;
                }
            }

            if (menuExist == false) {
                // add the selectedMenu to the listOfOrder
                listOfOrder.add(selectedMenu);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You have added " + selectedMenu.getName() + " to your order");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please select a menu");
            alert.show();
        }

        table.getSelectionModel().select(null);

        System.out.println(listOfOrder.get(0).getTimesOrdered());
        System.out.println(listOfOrder);
    }

    void setTableNum(String tableNum) {
        this.tableNum = Integer.parseInt(tableNum.trim());
    }

    @FXML
    void doneBtn(ActionEvent event) {
        if (listOfOrder.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please select a menu");
            alert.show();
        } else {
            try {
                // ambil fxml yang dituju
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderConfirmationPage.fxml"));
                // load fxml
                Parent root = loader.load();
                // pangil controller
                OrderConfirmationController controller = loader.getController();
                // set tableNum
                controller.setListOfOrderAndTableNumber(listOfOrder, this.tableNum);
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

}
