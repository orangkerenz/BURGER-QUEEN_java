package controller;

import java.sql.Connection;
import java.sql.ResultSet;
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

public class OrderRequestViewController {

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private TableColumn<Menu, Integer> quantiityCol;

    @FXML
    private TableColumn<Menu, String> menuNameCol;

    @FXML
    private TableView<Menu> table;

    private Order order;

    @FXML
    public void initialize() {
        menuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantiityCol.setCellValueFactory(new PropertyValueFactory<>("timesOrdered"));
    }

    @FXML
    void doneOnAction(ActionEvent event) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            String sql = "UPDATE `orders` SET `status` = 'order_recieved' WHERE orders.id = " + order.getId();
            int affectedRows = stmt.executeUpdate(sql);

            if (affectedRows > 0) {
                AlertTools.setAlert("Success!", null, "Order Have Been Mark Done!", AlertType.INFORMATION);

                JavafxTools.changeSceneActionEvent(event, "../view/OrderRequestPage.fxml");
            } else {
                AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/OrderRequestPage.fxml");
    }

    void setOrder(Order order) {
        this.order = order;

        setText();

        setTable();
    }

    private void setText() {
        orderIdText.setText("Order Id #" + String.valueOf(order.getId()));
        tableText.setText("Table Number #" + String.valueOf(order.getTableNum()));
    }

    private void setTable() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT * FROM menus_has_orders, menus WHERE menus_has_orders.menu_id = menus.id AND menus_has_orders.order_id = "
                            + order.getId());

            while (rs.next()) {
                Menu menu = new Menu(rs.getString("menus.name"), rs.getInt("menus_has_orders.quantity"));
                table.getItems().add(menu);
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
