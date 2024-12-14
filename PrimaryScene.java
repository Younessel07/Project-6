import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryScene {
    private Stage primaryStage;
    private String username;
    private Client client;
    private TextArea chatArea;

    public PrimaryScene(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
        connectToServer();
    }

    private void connectToServer() {
        try {
            client = new Client("localhost", 3200, username);

            // Start a thread to listen for incoming messages
            Thread listener = new Thread(() -> {
                try {
                    while (true) {
                        String message = client.receiveMessage();
                        if (message != null) {
                            chatArea.appendText(message + "\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Connection closed.");
                }
            });
            listener.setDaemon(true);
            listener.start();
        } catch (Exception e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public void show() {
        // Chat display area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPromptText("Chat messages will appear here...");

        // Input field, username label, and send button
        Label usernameLabel = new Label(username); // Label displaying the user's username
        TextField messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        Button sendButton = new Button("Send");

        // Layout for message input
        HBox messageInputBox = new HBox(10, usernameLabel, messageField, sendButton);
        messageInputBox.setAlignment(Pos.CENTER_LEFT);

        // Action for the send button
        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                client.sendMessage(message); // Send the raw message to the server
                chatArea.appendText("You: " + message + "\n");
                messageField.clear();
            }
        });

        // Main layout
        VBox layout = new VBox(10, chatArea, messageInputBox);
        layout.setStyle("-fx-padding: 10px;");
        Scene primaryScene = new Scene(layout, 500, 400);

        // Set the scene
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Primary Scene");
        primaryStage.show();
    }
}