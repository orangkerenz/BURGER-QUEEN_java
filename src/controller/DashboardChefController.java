package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tools.DatabaseTools;
import tools.JavafxTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardChefController {

    @FXML
    private DatePicker dateEnd;

    @FXML
    private DatePicker dateStart;

    @FXML
    private Text favoriteMenu;

    @FXML
    private Text leastFavoriteMenu;

    public void initialize() {
        System.out.println("halo1");
        try {
            System.out.println("halo2");
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT menus.name, SUM(menus_has_orders.quantity) AS total_quantity FROM menus_has_orders INNER JOIN menus ON menus_has_orders.menu_id = menus.id  INNER JOIN orders ON menus_has_orders.order_id = orders.id WHERE orders.paid = 1 GROUP BY menus_has_orders.menu_id ORDER BY total_quantity DESC LIMIT 1";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                System.out.println("halo3");
                favoriteMenu.setText(rs.getString("menus.name"));
            }
            System.out.println("halo4");

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT menus.name, SUM(menus_has_orders.quantity) AS total_quantity FROM menus_has_orders INNER JOIN menus ON menus_has_orders.menu_id = menus.id  INNER JOIN orders ON menus_has_orders.order_id = orders.id WHERE orders.paid = 1 GROUP BY menus_has_orders.menu_id ORDER BY total_quantity ASC LIMIT 1";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                leastFavoriteMenu.setText(rs.getString("menus.name"));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuChefPage.fxml");
    }

    @FXML
    void search(ActionEvent event) {

        String dateStart = null;
        String dateEnd = null;

        try {
            dateStart = this.dateStart.getValue().toString();
            dateEnd = this.dateEnd.getValue().toString();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Both Date");
            alert.show();

            return;
        }

        if (this.dateStart != null && this.dateEnd != null) {

            try {
                Connection conn = DatabaseTools.getConnection();
                Statement statement = conn.createStatement();

                String sql = "SELECT menus.name, SUM(menus_has_orders.quantity) AS total_quantity FROM menus_has_orders INNER JOIN menus ON menus_has_orders.menu_id = menus.id  INNER JOIN orders ON menus_has_orders.order_id = orders.id WHERE orders.paid = 1 AND DATE(orders.order_date) BETWEEN '"
                        + this.dateStart.getValue()
                        + "' AND '" + this.dateEnd.getValue()
                        + "' GROUP BY menus_has_orders.menu_id ORDER BY total_quantity DESC LIMIT 1";

                ResultSet rs = statement.executeQuery(sql);

                if (rs.next()) {
                    favoriteMenu.setText(rs.getString("menus.name"));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("No data found");
                    alert.show();
                }

                DatabaseTools.closeQueryOperation(conn, statement, rs);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Connection conn = DatabaseTools.getConnection();
                Statement statement = conn.createStatement();

                String sql = "SELECT menus.name, SUM(menus_has_orders.quantity) AS total_quantity FROM menus_has_orders INNER JOIN menus ON menus_has_orders.menu_id = menus.id  INNER JOIN orders ON menus_has_orders.order_id = orders.id WHERE orders.paid = 1 AND DATE(orders.order_date) BETWEEN '"
                        + this.dateStart.getValue()
                        + "' AND '" + this.dateEnd.getValue()
                        + "' GROUP BY menus_has_orders.menu_id ORDER BY total_quantity ASC LIMIT 1";

                ResultSet rs = statement.executeQuery(sql);

                if (rs.next()) {
                    leastFavoriteMenu.setText(rs.getString("menus.name"));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("No data found");
                    alert.show();
                }

                DatabaseTools.closeQueryOperation(conn, statement, rs);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Both Date");
            alert.show();
        }

    }

}
