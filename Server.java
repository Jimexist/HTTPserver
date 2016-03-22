import java.io.*;
import java.net.*;

public class Server {

    public static ServerSocket server;
    public static Socket sock;
    public static PrintWriter out;

    public static void main(String[] args) {

        try {
            server = new ServerSocket(8080);
            System.out.println("Running ... ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            sock = server.accept();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            try {
                InputStream sis = sock.getInputStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(sis));

                String request = br.readLine();
                String[] requestParam = request.split(" ");
                String path = requestParam[1];

                out = new PrintWriter(sock.getOutputStream(), true);

                oneClient(path);

            } catch (Exception e) {
                System.out.println(e.getMessage());            }
        }
    }

    public static void oneClient(String path) {

        File file = new File(path);

        FileReader fr = null;

        try {
            fr = new FileReader(file);
        } catch (Exception e) {
            out.println("HTTP 404");
            return;
        }

        BufferedReader bfr = new BufferedReader(fr);

        String line = "";

        while (true) {
            try {
                line = bfr.readLine();
                if (line == null) {
                    return;
                }
            } catch (Exception e) {
                return;
            }
            out.println(line);
        }

    }
}