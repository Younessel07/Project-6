import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ClientTest {

    @Test
    void testClientInitialization() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3200)) {
            Thread clientThread = new Thread(() -> {
                try {
                    Client client = new Client("localhost", 3200, "TestUser");
                    assertNotNull(client, "Client should initialize successfully.");
                } catch (Exception e) {
                    fail("Client initialization failed.");
                }
            });
            clientThread.start();

            Socket serverSideSocket = serverSocket.accept();
            assertNotNull(serverSideSocket, "Server should accept client connection.");
        }
    }

    @Test
    void testSendMessage() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(3200)) { // Use static port for simplicity
            Thread serverThread = new Thread(() -> {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    // assert the username message
                    assertEquals("USERNAME:TestUser", reader.readLine(), "Server should receive the username message.");

                    // assert the actual message
                    assertEquals("MESSAGE:Hello", reader.readLine(), "Server should receive the correct message.");
                } catch (IOException e) {
                    e.printStackTrace(); // Log exceptions for debugging
                    fail("Server failed to process the message.");
                }
            });
            serverThread.start();

            // Client sends a username and a message
            Client client = new Client("localhost", 3200, "TestUser");
            client.sendMessage("Hello");
        }
    }

    @Test
    void testReceiveMessage() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(3200)) {
            Thread serverThread = new Thread(() -> {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    writer.println("MESSAGE:Hi");
                } catch (IOException e) {
                    fail("Server failed to send a message.");
                }
            });
            serverThread.start();

            Client client = new Client("localhost", 3200, "TestUser");
            String message = client.receiveMessage();
            assertEquals("MESSAGE:Hi", message, "Client should receive the server's message.");
        }
    }
}