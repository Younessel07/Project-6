import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerTest {

    @Test
    void testServerInitialization() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3200)) {
            assertNotNull(serverSocket, "Server should initialize without errors.");
        }
    }

    @Test
    void testClientConnection() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3200)) {
            Thread clientThread = new Thread(() -> {
                try {
                    new Socket("localhost", 3200);
                } catch (IOException e) {
                    fail("Client connection failed.");
                }
            });
            clientThread.start();

            Socket client = serverSocket.accept();
            assertNotNull(client, "Client should connect to the server.");
        }
    }
}