package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Menu;
import model.Order;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

import java.io.IOException;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

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

    // isinya cuman orderId Dan Table Number
    private Order OrderIdAndTableNumber;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void addBtn(ActionEvent event) {
        Menu selectedOrder = table.getSelectionModel().getSelectedItem();
        boolean queryRes = false;
        try {
            Connection conn = DatabaseTools.getConnection();
            if (selectedOrder != null) {
                Statement stmtQueryResepMenu = conn.createStatement();
                ResultSet rsQueryResepMenu = stmtQueryResepMenu
                        .executeQuery("SELECT * FROM recipes WHERE menu_id = " + selectedOrder.getId());

                while (rsQueryResepMenu.next()) {
                    Statement stmtQueryIngredients = conn.createStatement();
                    ResultSet rsQueryIngredients = stmtQueryIngredients
                            .executeQuery(
                                    "SELECT * FROM ingredients WHERE id = " + rsQueryResepMenu.getInt("ingredient_id"));

                    if (rsQueryIngredients.next()) {

                        if (rsQueryIngredients.getDouble("quantity_in_grams") < rsQueryResepMenu
                                .getDouble("quantity_in_grams")) {
                            AlertTools.setAlert("Error!", null, "Menu Cant Be Order!", AlertType.ERROR);
                            return;
                        } else {

                            Statement stmtUpdateIngredients = conn.createStatement();
                            int affectedRows = stmtUpdateIngredients.executeUpdate(
                                    "UPDATE ingredients SET quantity_in_grams = quantity_in_grams - "
                                            + rsQueryResepMenu.getDouble("quantity_in_grams") + " WHERE id = "
                                            + rsQueryResepMenu.getInt("ingredient_id"));

                            if (affectedRows > 0) {
                                queryRes = true;
                            } else {
                                AlertTools.setAlert("Error!", null, "Please Contact Support!", AlertType.ERROR);
                                return;
                            }

                        }

                    }

                }

                if (queryRes == true) {
                    Statement queryMenusHasOrder = conn.createStatement();
                    ResultSet rsQueryMenusHasOrder = queryMenusHasOrder
                            .executeQuery("SELECT * FROM menus_has_orders WHERE menu_id = "
                                    + selectedOrder.getId() + " AND order_id = "
                                    + OrderIdAndTableNumber.getId());

                    if (rsQueryMenusHasOrder.next()) {
                        Statement updateMenusHasOrder = conn.createStatement();
                        updateMenusHasOrder
                                .executeUpdate(
                                        "UPDATE menus_has_orders SET quantity = quantity + 1 WHERE menu_id = "
                                                + selectedOrder.getId() + " AND order_id = "
                                                + OrderIdAndTableNumber.getId());
                    } else {
                        Statement insertMenusHasOrder = conn.createStatement();
                        insertMenusHasOrder.executeUpdate(
                                "INSERT INTO menus_has_orders (menu_id, order_id, quantity) VALUES ("
                                        + selectedOrder.getId() + ", "
                                        + OrderIdAndTableNumber.getId()
                                        + ", 1)");
                    }
                }

                selectedOrder.setTimesOrdered(selectedOrder.getTimesOrdered() + 1);
                table.refresh();

            } else {
                AlertTools.setAlert("Error!", null, "No Order Selected!", AlertType.ERROR);
            }
        } catch (

        Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void deleteteBtn(ActionEvent event) {
        Menu selectedOrder = table.getSelectionModel().getSelectedItem();
        boolean queryRes = false;
        Connection conn = DatabaseTools.getConnection();
        if (selectedOrder != null && selectedOrder.getTimesOrdered() > 0) {
            try {
                Statement stmtQueryResepMenu = conn.createStatement();
                ResultSet rsQueryResepMenu = stmtQueryResepMenu
                        .executeQuery("SELECT * FROM recipes WHERE menu_id = " + selectedOrder.getId());

                while (rsQueryResepMenu.next()) {
                    Statement stmtQueryIngredients = conn.createStatement();
                    ResultSet rsQueryIngredients = stmtQueryIngredients
                            .executeQuery(
                                    "SELECT * FROM ingredients WHERE id = " + rsQueryResepMenu.getInt("ingredient_id"));

                    if (rsQueryIngredients.next()) {

                        Statement stmtUpdateIngredients = conn.createStatement();
                        int affectedRows = stmtUpdateIngredients.executeUpdate(
                                "UPDATE ingredients SET quantity_in_grams = quantity_in_grams + "
                                        + rsQueryResepMenu.getDouble("quantity_in_grams") + " WHERE id = "
                                        + rsQueryResepMenu.getInt("ingredient_id"));

                        if (affectedRows > 0) {
                            queryRes = true;
                        } else {
                            AlertTools.setAlert("Error!", null, "Please Contact Support!", AlertType.ERROR);
                            return;
                        }
                    }
                }

                if (queryRes == true) {
                    Statement queryMenusHasOrder = conn.createStatement();
                    ResultSet rsQueryMenusHasOrder = queryMenusHasOrder
                            .executeQuery("SELECT * FROM menus_has_orders WHERE menu_id = "
                                    + selectedOrder.getId() + " AND order_id = "
                                    + OrderIdAndTableNumber.getId());
                    if (rsQueryMenusHasOrder.next()) {
                        if (rsQueryMenusHasOrder.getInt("quantity") == 1) {
                            Statement deleteMenusHasOrder = conn.createStatement();
                            deleteMenusHasOrder.executeUpdate(
                                    "DELETE FROM menus_has_orders WHERE menu_id = "
                                            + selectedOrder.getId() + " AND order_id = "
                                            + OrderIdAndTableNumber.getId());
                        } else {
                            Statement updateMenusHasOrder = conn.createStatement();
                            updateMenusHasOrder
                                    .executeUpdate(
                                            "UPDATE menus_has_orders SET quantity = quantity - 1 WHERE menu_id = "
                                                    + selectedOrder.getId() + " AND order_id = "
                                                    + OrderIdAndTableNumber.getId());
                        }
                    }
                }

                selectedOrder.setTimesOrdered(selectedOrder.getTimesOrdered() - 1);
                table.refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else

        {
            AlertTools.setAlert("Error!", null, "No Order Selected!", AlertType.ERROR);
        }
    }

    @FXML
    void doneBtn(ActionEvent event) {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmtQueryMenusHasOrder = conn.createStatement();
            ResultSet rsQueryMenusHasOrder = stmtQueryMenusHasOrder
                    .executeQuery("SELECT * FROM menus_has_orders WHERE order_id = "
                            + OrderIdAndTableNumber.getId());

            if (rsQueryMenusHasOrder.next()) {
                try {

                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("../view/OrderConfirmationPage.fxml"));

                    Parent root = loader.load();

                    OrderConfirmationController orderConfirmationController = loader.getController();

                    orderConfirmationController.setOrderId(OrderIdAndTableNumber.getId());

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                AlertTools.setAlert("Error!", null, "No Order Selected!", AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/TableNumberConfirmationPage.fxml");

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement stmt = conn.createStatement();
            int affectedRows = stmt.executeUpdate(
                    "UPDATE tables SET available = 1 WHERE table_number = " + OrderIdAndTableNumber.getTableNum());

            if (affectedRows > 0) {
                AlertTools.setAlert("Success!", null, "Your Table Is Available For Others!",
                        AlertType.INFORMATION);

                affectedRows = stmt.executeUpdate(
                        "UPDATE orders SET status = 'canceled' WHERE id = " + OrderIdAndTableNumber.getId());

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

    void setOrder(Order order) {
        this.OrderIdAndTableNumber = order;

        setTable();
    }

    private void setTable() {
        table.getItems().clear();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM menus WHERE available = 1");

            while (rs.next()) {
                Connection conn2 = DatabaseTools.getConnection();
                Statement stmt2 = conn2.createStatement();
                ResultSet rs2 = stmt2.executeQuery(
                        "SELECT count(*) FROM recipes WHERE menu_id = '" + rs.getInt("id") + "'");
                rs2.next();
                int recipesCount = rs2.getInt(1);

                Connection conn3 = DatabaseTools.getConnection();
                Statement stmt3 = conn3.createStatement();
                ResultSet rs3 = stmt3.executeQuery(
                        "SELECT count(*) FROM recipes, ingredients WHERE recipes.menu_id = '" + rs.getInt("id")
                                + "' AND recipes.quantity_in_grams <= ingredients.quantity_in_grams AND recipes.ingredient_id = ingredients.id");
                rs3.next();
                int ingredientsCount = rs3.getInt(1);

                if (ingredientsCount == recipesCount) {
                    table.getItems().add(new Menu(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
                }

                DatabaseTools.closeQueryOperation(conn2, stmt2, rs2);
                DatabaseTools.closeQueryOperation(conn3, stmt3, rs3);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (SQLException e) {
            AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);

            e.printStackTrace();
        }
    }
}
