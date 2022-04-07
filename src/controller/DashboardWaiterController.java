package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tools.DatabaseTools;
import tools.JavafxTools;

public class DashboardWaiterController {

    @FXML
    private Text orderServedToday;

    @FXML
    private Text tablesAreAvailable;

    public void initialize() {
        try {
            Connection conn = DatabaseTools.getConnection();
            Statement statement = conn.createStatement();

            String sql = "SELECT COUNT(*) FROM tables WHERE available = 1";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                tablesAreAvailable.setText(rs.getString(1));
            }

            DatabaseTools.closeQueryOperation(conn, statement, rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(MouseEvent event) {
        JavafxTools.changeSceneMouseEvent(event, "../view/MenuWaiterPage.fxml");
    }

}
