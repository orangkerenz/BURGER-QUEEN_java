package tools;

import java.io.IOException;
import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import javafx.stage.Stage;

public class JavafxTools {
    public static void changeSceneMouseEvent(MouseEvent event, String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(JavafxTools.class.getResource(fxmlFileName));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneAnchorPane(AnchorPane ac, String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(JavafxTools.class.getResource(fxmlFileName));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ac.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneActionEvent(ActionEvent event, String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(JavafxTools.class.getResource(fxmlFileName));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTextFieldEmpty(TextField... tf) {
        for (TextField textField : tf) {
            textField.setText("");
        }
    }

    public static Connection getConnection() {
        return null;
    }

}
