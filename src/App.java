import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/WelcomePage.fxml"));
        // load fxml
        Parent root = loader.load();
        // create scene
        Scene scene = new Scene(root);
        // set scene to stage
        primaryStage.setScene(scene);
        // show stage
        primaryStage.show();
    }

}
