package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    private LinkedList<Menu> menuList = new LinkedList<Menu>();

    private LinkedList<Menu> listOfOrder = new LinkedList<Menu>();

    private Integer tableNum;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));

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
        boolean isExist = false;
        for (Menu menu : listOfOrder) {
            if (menu.getId() == selectedMenu.getId()) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            listOfOrder.add(selectedMenu);
            selectedMenu.setTimesOrdered(selectedMenu.getTimesOrdered() + 1);
        } else {
            selectedMenu.setTimesOrdered(selectedMenu.getTimesOrdered() + 1);

        }

        System.out.println("Selected Menu 3 Times Ordered : " + selectedMenu.getTimesOrdered());
        table.getItems().clear();

        table.getItems().addAll(menuList);

    }

    @FXML
    void deleteteBtn(ActionEvent event) {
        Menu selectedMenu = table.getSelectionModel().getSelectedItem();
        boolean isExist = false;
        if (selectedMenu != null) {
            for (Menu menu : listOfOrder) {
                if (menu.getId() == selectedMenu.getId()) {
                    isExist = true;
                    break;
                }
            }

            if (selectedMenu.getTimesOrdered() > 0) {
                selectedMenu.setTimesOrdered(selectedMenu.getTimesOrdered() - 1);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No menu selected");
                alert.setContentText("Please select a menu to delete from your order");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Menu Is Empty");
            alert.setContentText("Please select menu is bigger than 0");
            alert.showAndWait();
        }

        table.getItems().clear();

        table.getItems().addAll(menuList);

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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to order?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
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

                    stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            new Timer().schedule(
                                    new TimerTask() {

                                        @Override
                                        public void run() {
                                            // check apakah dia sudah di paid atau belum(Belum selesai)

                                            // jika sudah maka
                                            Connection connection = GetConnection.getConnection();
                                            String sql = "UPDATE tables SET avaliable = 1 WHERE tables_num = "
                                                    + tableNum;
                                            Statement statement = null;

                                            try {
                                                statement = connection.createStatement();
                                                statement.executeUpdate(sql);
                                                connection.close();
                                                statement.close();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            cancel();

                                            System.out.println("pagi");
                                        }

                                    }, 60000, 60000);

                        }
                    });

                    // buat scene baru dan tempelin root yang ingin dituju
                    Scene scene = new Scene(root);
                    // stage yang sekarang ambil dan tempelin scene yang baru/ingin dituju
                    stage.setScene(scene);
                    // show stage yang baru
                    stage.show();

                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Information Dialog");
                    alert2.setHeaderText(null);
                    alert2.setContentText(
                            "We Will Give 1 Minute To Finish Your Payment, Or Else Your Seat Will Be Available For Other Customer");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (Menu menu : menuList) {
                    menu.setTimesOrdered(0);
                }

                listOfOrder.clear();

                table.getItems().clear();

                table.getItems().addAll(menuList);

            }

        }
    }

    void setTableNum(String tableNum) {
        this.tableNum = Integer.parseInt(tableNum.trim());
    }

    @FXML
    void backBtn(MouseEvent event) {
        Connection connection = GetConnection.getConnection();
        String sql = "UPDATE tables SET avaliable = 1 WHERE tables_num = " + this.tableNum;

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/TableNumberConfirmationPage.fxml"));
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
