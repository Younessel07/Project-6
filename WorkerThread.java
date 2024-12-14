import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerThread extends Thread {
    private Socket client1;
    private Socket client2;
    private String username1;
    private String username2;

    public WorkerThread(Socket client1, Socket client2) {
        this.client1 = client1;
        this.client2 = client2;
    }

    public void run() {
        try (
            BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
            PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
        ) {
            // Read usernames from both clients
            username1 = in1.readLine().split(":")[1];
            username2 = in2.readLine().split(":")[1];
            System.out.println("Usernames: " + username1 + ", " + username2);

            // Relay messages between clients
            while (true) {
                String messageFrom1 = in1.readLine();
                if (messageFrom1 != null && messageFrom1.startsWith("MESSAGE:")) {
                    out2.println(username1 + ": " + messageFrom1.substring(8));
                }

                String messageFrom2 = in2.readLine();
                if (messageFrom2 != null && messageFrom2.startsWith("MESSAGE:")) {
                    out1.println(username2 + ": " + messageFrom2.substring(8));
                }
            }
        } catch (Exception e) {
            System.out.println("Connection lost: " + e.getMessage());
        }
    }
}