import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Server class gets HTTP request and sends back data.
 * @author Jiashen Cao
 */
public class Server {

    public static ServerSocket server;
    public static Socket sock;
    public static PrintWriter out;

    /**
     * Connect to Socket Server and accepts client.
     * @param args Input from console.
     */
    public static void main(String[] args) {

        // Bind the server
        try {
            server = new ServerSocket(8080);
            System.out.println("Running ... ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Accept clients
        try {
            sock = server.accept();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Listen to the request
        while (true) {
            try {
                InputStream sis = sock.getInputStream();
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(sis));

                // Get path of requested file
                String request = br.readLine();
                String[] requestParam = request.split(" ");
                String path = requestParam[1];

                out = new PrintWriter(sock.getOutputStream(), true);

                // Use different method to send back data
                redirect(path);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Redirect client according to different requested file.
     * @param path The real path of local file
     */
    public static void redirect(String path) {

        String[] pathString = path.split("/");
        String endpath = pathString[pathString.length - 1];

        System.out.println(endpath);

        String[] seppath = endpath.split("[.]");

        // Check the format of a file
        String format = seppath[1];

        if (format == null) {
            plainText(path);
        } else if (format.equals("png") || format.equals("png")) {
            image(path);
        } else if (format.equals("html")) {
            System.out.println(path);
            html(path);
        }
    }

    /**
     * Send back plain text data.
     * @param path The real path of data
     */
    public static void plainText(String path) {

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

        // Send back data
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

    public static void image(String path) {

    }

    /**
     * Send back HTML header data
     * @param path Real path of a file
     */
    public static void html(String path) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("\n");
        plainText(path);
    }
}