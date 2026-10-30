import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage stage) {
        UserDAO userDao = new UserDAO();

        Label title = new Label("Online Food Ordering System");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label messageLabel = new Label();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password.");
                return;
            }

            User user = userDao.login(username, password);

            if (user == null) {
                messageLabel.setText("Invalid username or password.");
                return;
            }

            // POLYMORPHISM: same method call, different message for Customer / Admin
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(user.getWelcomeMessage());
            alert.showAndWait();

            Stage next = new Stage();
            if (user instanceof Admin) {
                new AdminScreen().start(next);
            } else {
                new MenuScreen().start(next);
            }
            stage.close();
        });

        VBox root = new VBox(12, title, usernameField, passwordField, loginButton, messageLabel);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 360, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}