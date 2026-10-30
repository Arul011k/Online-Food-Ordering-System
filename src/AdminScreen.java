import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AdminScreen extends Application {

    @Override
    public void start(Stage stage) {
        OrderDAO dao = new OrderDAO();
        ArrayList<String> orders = dao.getAllOrders();

        Label title = new Label("All Customer Orders (Admin View)");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            new LoginScreen().start(new Stage());
            stage.close();
        });

        HBox topBar = new HBox(10, title, spacer, logoutButton);

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(orders);
        VBox.setVgrow(listView, Priority.ALWAYS);

        VBox root = new VBox(10, topBar, listView);
        root.setPadding(new Insets(15));

        stage.setTitle("Admin - Orders");
        stage.setScene(new Scene(root, 800, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}