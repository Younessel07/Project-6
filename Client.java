import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    public Client(String serverAddress, int port, String username) throws Exception {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());

        // Send the username to the server
        out.println("USERNAME:" + username);
    }

    public void sendMessage(String message) {
        out.println("MESSAGE:" + message);
    }

    public String receiveMessage() {
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return null;
    }

    public void close() throws Exception {
        socket.close();
    }
}