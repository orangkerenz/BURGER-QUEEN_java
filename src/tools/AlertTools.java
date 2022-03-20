package tools;

import javafx.scene.control.Alert;

public class AlertTools {
    public static final String AlertType = null;

    public static void setAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
