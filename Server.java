import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Server class binds the server address and accepts client.
 * @author Jiashen Cao
 */
public class Server{

    public static ServerSocket server;

    /**
     * Connect to Socket Server and accepts client.
     * @param args Input from console.
     */
    public static void main(String[] args) {

        // Bind the server
        try {
            server = new ServerSocket(8080);
            System.out.println("Running ... ");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Accept clients
        while (true) {
            try {
                Socket sock = server.accept();
                new Thread(new MultithreadingServer(sock)).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            try{
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}