import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class WorkerThreadTest {

    @Test
    void testMessageRelay() throws Exception {
    	
        // Start a server socket for testing
        try (ServerSocket serverSocket = new ServerSocket(3201)) { 
            // Start client 1
            Thread client1Thread = new Thread(() -> {
                try (Socket client1Socket = new Socket("localhost", 3201);
                     PrintWriter out = new PrintWriter(client1Socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()))) {
                    
                    // Send a message to the server
                    out.println("MESSAGE:Hello from Client1");
                    
                    // Read the message relayed by the server
                    String received = in.readLine();
                    assertEquals("Client2: Hello from Client2", received, "Client 1 should receive Client 2's message.");
                } catch (IOException e) {
                    fail("Client 1 failed: " + e.getMessage());
                }
            });

            // Start client 2
            Thread client2Thread = new Thread(() -> {
                try (Socket client2Socket = new Socket("localhost", 3201);
                     PrintWriter out = new PrintWriter(client2Socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()))) {
                    
                    // Read the message relayed by the server
                    String received = in.readLine();
                    assertEquals("Client1: Hello from Client1", received, "Client 2 should receive Client 1's message.");
                    
                    // Send a message to the server
                    out.println("MESSAGE:Hello from Client2");
                } catch (IOException e) {
                    fail("Client 2 failed: " + e.getMessage());
                }
            });

            // Start the worker thread
            Thread workerThread = new Thread(() -> {
                try (Socket client1 = serverSocket.accept();
                     Socket client2 = serverSocket.accept()) {
                    WorkerThread workerThreadInstance = new WorkerThread(client1, client2);
                    workerThreadInstance.run();
                } catch (IOException e) {
                    fail("WorkerThread failed: " + e.getMessage());
                }
            });

            // Start threads
            workerThread.start();
            client1Thread.start();
            client2Thread.start();

            // Wait for threads to finish
            workerThread.join();
            client1Thread.join();
            client2Thread.join();
            
            assertTrue(true, "Test completed successfully!");
        }
    }
}