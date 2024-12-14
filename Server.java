import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 3200;

    public static void main(String[] args) {
        System.out.println("Server is running on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for Client 1...");
                Socket client1 = serverSocket.accept();
                System.out.println("Client 1 connected.");

                System.out.println("Waiting for Client 2...");
                Socket client2 = serverSocket.accept();
                System.out.println("Client 2 connected.");

                // Create a thread to handle communication between the two clients
                WorkerThread workerThread = new WorkerThread(client1, client2);
                workerThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error in Server: " + e.getMessage());
        }
    }
}