package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import tools.AlertTools;
import tools.CurrentLoginUser;
import tools.DatabaseTools;
import tools.JavafxTools;

public class LeavingController {

    @FXML
    private Button leavingBtn;

    @FXML
    private Text orderIdText;

    @FXML
    private Text tableText;

    @FXML
    private Text informationText;

    private int order_id;

    private int table_num;

    @FXML
    void leavingOnAction(ActionEvent event) {
        JavafxTools.changeSceneActionEvent(event, "../view/ThankyouPage.fxml");

        CurrentLoginUser.setCurrentUser(null);

        Connection conn = DatabaseTools.getConnection();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE tables SET available = 1 WHERE table_number = " + table_num);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AlertTools.setAlert("Alert!", null, "You Table Is Now Available For Other Customer!", AlertType.INFORMATION);
    }

    void setOrderId(int order_id) {
        this.order_id = order_id;

        setOrderIdText();

        setTableNum();

        setTableNumText();

        setInformationText();
    }

    private void setOrderIdText() {
        orderIdText.setText(String.valueOf(order_id));
    }

    private void setTableNum() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            String sql = "SELECT table_number FROM orders WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, order_id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                table_num = rs.getInt("table_number");
            }

            DatabaseTools.closeQueryOperation(conn, pstmt, rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTableNumText() {
        tableText.setText("Hi, Table #" + String.valueOf(table_num));
    }

    private void setInformationText() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseTools.getConnection();
            String sql = "SELECT status FROM orders WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, order_id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                informationText.setText("Order Status : " + rs.getString("status"));
            }

            DatabaseTools.closeQueryOperation(conn, pstmt, rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
