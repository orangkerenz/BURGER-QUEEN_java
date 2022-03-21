package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Text;

import model.Menu;
import model.Order;
import tools.AlertTools;
import tools.DatabaseTools;
import tools.JavafxTools;

public class ServedRequestViewController {

    @FXML
    private Text informationText;

    @FXML
    private TableColumn<Menu, Integer> menuNameCol;

    @FXML
    private TableColumn<Menu, Integer> quantityCol;

    @FXML
    private TableView<Menu> table;

    private Order order;

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void doneBtn(ActionEvent event) {
        Connection conn = null;
        Statement stmt = null;
        int affectedRows;

        try {

            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            affectedRows = stmt
                    .executeUpdate("UPDATE orders SET status = 'served' WHERE id = " + order.getId());

            if (affectedRows > 0) {
                AlertTools.setAlert("Succes", null, "Order Is Mark Served!", AlertType.INFORMATION);

                setTable();
            } else {
                AlertTools.setAlert("Error", null, "Error Contact Support!", AlertType.ERROR);
            }

            DatabaseTools.closeQueryOperation(conn, stmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/ServedRequestPage.fxml");
    }

    void setOrder(Order order) {
        this.order = order;

        setTable();

        setInformationText();
    }

    private void setInformationText() {
        informationText.setText("Order #" + order.getId() + " Table #" + order.getTableNum());
    }

    private void setTable() {
        table.getItems().clear();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM menus_has_orders, menus WHERE menus_has_orders.order_id = "
                    + order.getId() + " AND menus_has_orders.menu_id = menus.id");

            while (rs.next()) {
                Menu menu = new Menu(rs.getString("menus.name"),
                        rs.getInt("menus_has_orders.quantity"));
                table.getItems().add(menu);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
