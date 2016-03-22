import java.io.*;
import java.net.*;

public class Server {

    public static ServerSocket server;
    public static Socket sock;

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

                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

                File file = null;

                try {
                    file = new File(path);
                } catch (Exception e) {
                    out.println("HTTP 404");
                    continue;
                }

                FileReader fr = new FileReader(file);
                BufferedReader bfr = new BufferedReader(fr);

                String line = null;

                while ((line = bfr.readLine()) != null) {
                    out.println(line);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}