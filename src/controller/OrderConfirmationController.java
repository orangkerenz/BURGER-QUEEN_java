package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Menu;
import model.Order;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;

public class OrderConfirmationController {

    @FXML
    private AnchorPane anchorPane;

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
    private Text orderText;

    private int order_id;

    private double total_price;

    private int table_num;

    private int timerTaskRun;

    @FXML
    public void initialize() {
        timerTaskRun = 0;
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        // check apakah ordernya telah dibayar?
                        Order.cancelOrderBiggerThanOneDay();
                        try {
                            Connection conn = DatabaseTools.getConnection();
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE id = " + order_id);

                            if (rs.next()) {

                                if (rs.getString("status").equals("canceled") && timerTaskRun == 0) {
                                    Connection connGantiStatusTable = DatabaseTools.getConnection();
                                    Statement stmtGantiStatusTable = connGantiStatusTable.createStatement();
                                    stmtGantiStatusTable
                                            .executeUpdate("UPDATE tables SET available = '1' WHERE table_number = "
                                                    + table_num);

                                    informationText.setText("Order telah dibatalkan, \nMelebihi Batas Waktu!");

                                    System.out.println("saya terbaru!");

                                    cancelBtn.setVisible(false);

                                    timerTaskRun++;

                                }

                                if (rs.getInt("paid") == 0 && !rs.getString("status").equals("canceled")) {
                                    doneBtn.setVisible(false);
                                    cancelBtn.setVisible(true);
                                }

                                if (rs.getInt("paid") == 1 && rs.getString("Status").equals("pending")) {
                                    // order telah dibayar
                                    doneBtn.setVisible(false);
                                    cancelBtn.setVisible(false);
                                    informationText.setText("Order Status : Paid!");
                                    informationText.setVisible(true);
                                }

                                if (rs.getInt("paid") == 1 && rs.getString("status").equals("ready")) {
                                    // order telah dibayar
                                    cancelBtn.setVisible(false);
                                    informationText.setText("Order Status : Order Is Ready!");
                                    informationText.setVisible(true);
                                }

                                if (rs.getInt("paid") == 1 && rs.getString("status").equals("served")) {
                                    // order telah dibayar
                                    doneBtn.setVisible(true);
                                    cancelBtn.setVisible(false);
                                    informationText.setText("Order Status : Order Is Served!");
                                    informationText.setVisible(true);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 5000);

    }

    @FXML
    void cancelOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/TableNumberConfirmationPage.fxml");

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement queryOrders = conn.createStatement();
            ResultSet rsOrders = queryOrders
                    .executeQuery(
                            "SELECT * FROM orders WHERE id = '" + order_id + "'");

            while (rsOrders.next()) {
                Statement queryMenu = conn.createStatement();
                ResultSet rsMenu = queryMenu
                        .executeQuery(
                                "SELECT * FROM menus_has_orders WHERE order_id = '" + rsOrders.getInt("id") + "'");

                while (rsMenu.next()) {
                    Statement queryRecipeMenu = conn.createStatement();
                    ResultSet rsRecipeMenu = queryRecipeMenu
                            .executeQuery(
                                    "SELECT * FROM recipes WHERE menu_id = '" + rsMenu.getInt("menu_id") + "'");
                    while (rsRecipeMenu.next()) {
                        double quantityWantToReturn = rsRecipeMenu.getDouble("quantity_in_grams")
                                * rsMenu.getInt("quantity");

                        Statement updateRecipe = conn.createStatement();
                        updateRecipe.executeUpdate(
                                "UPDATE ingredients SET quantity_in_grams = quantity_in_grams + "
                                        + quantityWantToReturn
                                        + " WHERE id = '" + rsRecipeMenu.getInt("ingredient_id") + "'");
                    }
                }

                Statement deleteMenusHasOrder = conn.createStatement();
                deleteMenusHasOrder.executeUpdate(
                        "DELETE FROM menus_has_orders WHERE order_id = '" + rsOrders.getInt("id") + "'");

                Statement cancelOrder = conn.createStatement();
                cancelOrder
                        .executeUpdate(
                                "UPDATE orders SET status = 'canceled' WHERE id = '" + rsOrders.getInt("id") + "'");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            int affectedRows = stmt.executeUpdate(
                    "UPDATE tables SET available = 1 WHERE table_number = " + table_num);

            if (affectedRows > 0) {
                AlertTools.setAlert("Success!", null, "Your Table Is Available For Others!",
                        AlertType.INFORMATION);

                affectedRows = stmt.executeUpdate(
                        "UPDATE orders SET status = 'canceled' WHERE id = " + order_id);

                if (affectedRows > 0) {
                    AlertTools.setAlert("Success!", null, "Your Order Is Canceled!", AlertType.INFORMATION);
                } else {
                    AlertTools.setAlert("Error!", null, "Your Order Is Not Canceled!", AlertType.ERROR);
                }

            } else {
                AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
            }

            DatabaseTools.closeQueryOperation(conn, stmt);

        } catch (SQLException e) {
            AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
        }
    }

    @FXML
    void doneOnAction(ActionEvent event) {
        setOrderToCompleted();

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../view/LeavingPage.fxml"));

            Parent root;

            root = loader.load();

            LeavingController leavingController = loader.getController();

            leavingController.setOrderId(order_id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @FXML
    void logoutBtn(ActionEvent event) {
        CurrentLoginUser.setCurrentUser(null);
        JavafxTools.changeSceneActionEvent(event, "../view/LoginPage.fxml");
    }

    private void setOrderToCompleted() {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE orders SET status = 'completed' WHERE id = " + order_id);

            DatabaseTools.closeQueryOperation(conn, stmt);

        } catch (SQLException e) {
            AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
        }
    }

    void setOrderId(int order_id) {
        this.order_id = order_id;

        setOrderText();

        setTable();

        setTotalPriceText();

        setTotalPriceInOrderTable();

        setInformationText();

        this.cancelBtn.setVisible(false);
        this.doneBtn.setVisible(false);
    }

    private void setTotalPriceText() {
        totalText.setText("Total : " + total_price);
    }

    private void setTotalPriceInOrderTable() {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE orders SET total_price = " + total_price + " WHERE id = " + order_id);

            DatabaseTools.closeQueryOperation(conn, stmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setInformationText() {

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM orders WHERE id = " + order_id;

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                informationText.setText(rs.getString("status"));

                table_num = rs.getInt("table_number");

            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        informationText.setText("Order Status : " + "Unpaid");
    }

    private void setOrderText() {
        orderText.setText("Order #" + order_id);
    }

    private void setTable() {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT menus_has_orders.quantity, menus.name, menus.price FROM menus_has_orders, menus WHERE menus.id = menus_has_orders.menu_id AND menus_has_orders.order_id = "
                    + order_id;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                table.getItems().add(new Menu(rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity")));
                total_price += rs.getDouble("price") * rs.getInt("quantity");

            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
