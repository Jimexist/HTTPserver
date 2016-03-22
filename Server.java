import java.io.*;
import java.net.*;

public class Server {

    public static ServerSocket server;

    public static void main(String[] args) {

        try {
            server = new ServerSocket(8080);
            System.out.println("Running ... ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        while (true) {
            try {
                Socket sock = serverSock.accept();

                InputStream sis = sock.getInputStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(sis));

                String request = br.readLine();
                String[] requestParam = request.split(" ");
                String path = requestParam[1];

                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                File file = new File(path);

                if (!file.exists()) {
                     out.write("HTTP 404");
                }

                FileReader fr = new FileReader(file);
                BufferedReader bfr = new BufferedReader(fr);
                String line;

                while ((line = bfr.readLine()) != null) {
                    out.write(line);
                }

                bfr.close();
                br.close();
                out.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}