package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import database.CurrentLoginUser;
import database.GetConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Menu;
import javafx.scene.Node;

public class OrderConfirmationController {

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableColumn<Menu, Double> priceCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    @FXML
    private Text totalText;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button doneBtn;

    @FXML
    private Text informationText;

    @FXML
    private Text orderIdText;

    private LinkedList<Menu> listOfOrder;

    private int tableNumber;

    private int orderId;

    private boolean done = false;

    private double total = 0;

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));

        this.doneBtn.setVisible(false);

        informationText.setText("Unpaid");

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        if (done == true) {
                            cancel();
                        }

                        Connection connection = GetConnection.getConnection();
                        String sql = "SELECT * FROM orders WHERE id = ?";
                        ResultSet resultSet = null;
                        PreparedStatement statement = null;

                        try {
                            statement = connection.prepareStatement(sql);
                            statement.setInt(1, orderId);
                            resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                if (resultSet.getInt("paid") == 1) {
                                    doneBtn.setVisible(true);
                                    cancelBtn.setVisible(false);
                                    newTransaction();
                                    informationText.setText("Paid");
                                    cancel();
                                    break;
                                }

                                if (resultSet.getInt("paid") == 0) {
                                    informationText.setText("Unpaid");
                                }

                            }
                            connection.close();
                            resultSet.close();
                            statement.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                }, 0, 10000);

    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        done = true;
        Connection connection = GetConnection.getConnection();
        String sql = "UPDATE orders SET canceled = 1 WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // set the table available to 1
        sql = "UPDATE tables SET avaliable = 1 WHERE tables_num = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.tableNumber);
            preparedStatement.executeUpdate();

            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // go back to the main menu
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/WelcomePage.fxml"));
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
    void doneOnAction(ActionEvent event) {
        done = true;

        // go back to the main menu
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LeavingPage.fxml"));
            // load fxml
            Parent root = loader.load();
            // ambil controllernya
            LeavingController controller = loader.getController();
            // set order id
            controller.setOrderIdText(this.orderId);
            // set table text
            controller.setTableText(this.tableNumber);
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
    void logoutBtn(ActionEvent event) {
        CurrentLoginUser.currentUser = null;
        // go back to the main menu
        try {
            // ambil fxml yang dituju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginPage.fxml"));
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

        CurrentLoginUser.currentUser = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Logout Success");
        alert.setContentText("You have been logged out");
        alert.showAndWait();
    }

    void setListOfOrderAndTableNumber(LinkedList<Menu> listOfOrder, int tableNumber) {
        this.listOfOrder = listOfOrder;
        this.tableNumber = tableNumber;

        this.total = 0;
        for (Menu menu : listOfOrder) {
            total += menu.getPrice() * menu.getTimesOrdered();
            table.getItems().add(menu);
        }

        totalText.setText("Total: $" + total);

        newOrder();

    }

    void newOrder() {

        try {
            Connection connection1 = GetConnection.getConnection();
            String sql1 = "INSERT INTO orders (order_date, paid, users_id, tables_num, canceled, total_price, customers_id) VALUES (?, 0, null, ?, 0, ?, ?)";

            PreparedStatement preparedStatement1 = connection1.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement1.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement1.setInt(2, this.tableNumber);
            preparedStatement1.setDouble(3, this.total);
            preparedStatement1.setInt(4, CurrentLoginUser.currentUser.getId());
            System.out.println("current user id " + CurrentLoginUser.currentUser.getId());
            preparedStatement1.executeUpdate();

            ResultSet res = preparedStatement1.getGeneratedKeys();

            if (res.next()) {
                this.orderId = res.getInt(1);
                this.orderIdText.setText("Order ID: " + this.orderId);

            }

            connection1.close();
            preparedStatement1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection connection2 = GetConnection.getConnection();
            PreparedStatement preparedStatement2 = null;

            for (Menu menu : listOfOrder) {
                String sql2 = "INSERT INTO menus_has_order (orders_id, menus_id, quantity) VALUES (?, ?, ?)";
                preparedStatement2 = connection2.prepareStatement(sql2);
                preparedStatement2.setInt(1, this.orderId);
                preparedStatement2.setInt(2, menu.getId());
                preparedStatement2.setInt(3, menu.getTimesOrdered());

                preparedStatement2.executeUpdate();
            }

            connection2.close();
            preparedStatement2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void newTransaction() {
        try {
            Connection connection = GetConnection.getConnection();
            String sql = "INSERT INTO transactions (transaction_date, price, orders_id, type) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setDouble(2, this.total);
            preparedStatement.setInt(3, this.orderId);
            preparedStatement.setString(4, "debit");

            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
