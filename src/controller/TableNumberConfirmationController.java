package controller;

import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Order;

public class TableNumberConfirmationController {

    @FXML
    private ComboBox<String> tableNumberCb;

    private int orderId;

    @FXML
    public void initialize() {
        setTableNumberCb();

    }

    @FXML
    void confirmButton(ActionEvent event) {
        setTableToUnAvailable(event);

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/WelcomePage.fxml");

        CurrentLoginUser.setCurrentUser(null);

    }

    public void setTableNumberCb() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM tables WHERE available = 1");

            while (rs.next()) {
                tableNumberCb.getItems()
                        .add(rs.getString("table_number") + "-" + "Capacity(" + rs.getString("table_capacity") + ")");
            }

            DatabaseTools.closeQueryOperation(conn, stmt, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTableToUnAvailable(ActionEvent event) {
        int table_number = 0;
        int affectedRows;

        try {
            table_number = Integer.parseInt(tableNumberCb.getValue().split("-")[0]);
        } catch (Exception e) {
            e.printStackTrace();
            AlertTools.setAlert("Error!", null, "Please Select A Table", AlertType.ERROR);

            return;
        }

        if (table_number != 0) {
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = DatabaseTools.getConnection();
                stmt = conn.createStatement();
                affectedRows = stmt
                        .executeUpdate("UPDATE tables SET available = 0 WHERE table_number = " + table_number);

                if (affectedRows > 0) {
                    setOrders(table_number, event);
                } else {

                    AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
                }

                DatabaseTools.closeQueryOperation(conn, stmt);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.setAlert("Error!", null, "Please Select A Table", AlertType.ERROR);

            return;
        }

    }

    public void setOrders(int table_number, ActionEvent event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows;

        try {
            conn = DatabaseTools.getConnection();
            stmt = conn.prepareStatement(
                    "INSERT INTO orders (order_date, paid, waiter_id, customer_id, table_number, total_price, status) VALUES (NOW(), 0, null, ?,?,0,'pending')",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, CurrentLoginUser.getId());
            stmt.setInt(2, table_number);

            affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                rs.next();
                this.orderId = rs.getInt(1);

                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("../view/SelectMenuPage.fxml"));

                    Parent root = loader.load();

                    SelectMenuController selectMenuController = loader.getController();

                    selectMenuController.setOrder(new Order(orderId, table_number));

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                AlertTools.setAlert("Error!", null, "Contact Support!", AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
