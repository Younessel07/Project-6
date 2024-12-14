import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScene extends Application {
    private Stage primaryStage;

    public void start(Stage stage) {
        this.primaryStage = stage;

        // Title label for the app
        Label titleLabel = new Label("SimpleChatApp");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Username label and text field
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Connect button
        Button connectButton = new Button("Connect to Server");
        connectButton.setOnAction(e -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                openPrimaryScene(username); // Pass username to the Primary Scene
            } else {
                System.out.println("Username cannot be empty!");
            }
        });

        // Layout and styling
        VBox layout = new VBox(15, titleLabel, usernameLabel, usernameField, connectButton);
        layout.setAlignment(Pos.CENTER); // Center all elements
        layout.setStyle("-fx-padding: 20px;");

        Scene startScene = new Scene(layout, 400, 300);

        // Set up the stage
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Start Scene");
        primaryStage.show();
    }

    private void openPrimaryScene(String username) {
        System.out.println("Connecting as: " + username);
        PrimaryScene primaryScene = new PrimaryScene(primaryStage, username);
        primaryScene.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}