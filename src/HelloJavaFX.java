import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label message = new Label("JavaFX is working!");
        StackPane root = new StackPane(message);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Food Ordering System - Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
