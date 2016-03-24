import java.io.*;
import java.net.*;

/**
 * Multithreading Server class process requests by using different threads.
 * @author Jiashen Cao
 */
public class MultithreadingServer implements Runnable {

    private Socket sock;
    private PrintWriter out;
    private boolean running;

    /**
     * Initial a socket with a thread and start that thread.
     * @param  socket The socket client.
     */
    public MultithreadingServer(Socket socket) {
        this.sock = socket;
        this.running = true;
    }

    /**
     * Process HTTP request.
     */
    public void processRequest() {
        try {
            InputStream sis = this.sock.getInputStream();
            BufferedReader br = new BufferedReader(
                new InputStreamReader(sis));

            // Get path of requested file
            String request = br.readLine();
            String[] requestParam = request.split(" ");
            String path = requestParam[1];

            this.out = new PrintWriter(sock.getOutputStream(), true);

            // Use different method to send back data
            redirect(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Redirect each request to different response.
     * @param path The real path of a file in computer.
     */
    public void redirect(String path) {
        String[] pathString = path.split("/");
        String endpath = pathString[pathString.length - 1];

        String[] seppath = endpath.split("[.]");

        String format = null;

        // Check the format of a file
        if (seppath.length >= 2) {
            format = seppath[1];
        } else {
            plainText(path);
        }

        if (format == null) {
            plainText(path);
        } else if (format.equals("jpeg")) {
            imageJPEG(path);
        } else if (format.equals("html")) {
            html(path);
        } else if (format.equals("js")) {
            javascript(path);
        } else if (format.equals("png")) {
            imagePNG(path);
        } else if (format.equals("css")) {
            css(path);
        }
    }

    /**
     * Send back plain text data.
     * @param path Real path of a file in computer.
     */
    public void plainText(String path) {
        File file = new File(path);

        FileReader fr = null;

        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            this.out.println("HTTP/1.1 200 OK");
            this.out.println("Content-Type: text/html");
            this.out.println("\n");
            this.out.println("Error 404");
        }

        BufferedReader bfr = new BufferedReader(fr);

        String line = "";

        // Send back data
        while (true) {
            try {
                line = bfr.readLine();
                if (line == null) {
                    break;
                }
            } catch (IOException e) {
                break;
            }
            this.out.println(line);
        }

        this.running = false;
    }

    /**
     * Send back jpeg image header.
     * @param path Real path of a file in computer.
     */
    public void imageJPEG(String path) {
        this.out.println("HTTP/1.1 200 OK");
        this.out.println("Content-Type: image/jpeg");
        plainText(path);
    }

    /**
     * Send back png image header.
     * @param path Real path of a file in computer.
     */
    public void imagePNG(String path) {
        this.out.println("HTTP/1.1 200 OK");
        this.out.println("Content-Type: image/png");
        plainText(path);
    }

    /**
     * Send back HTML header data.
     * @param path Real path of a file
     */
    public void html(String path) {
        this.out.println("HTTP/1.1 200 OK");
        this.out.println("Content-Type: text/html");
        this.out.println("\n");
        plainText(path);
    }

    /**
     * Send back JavaScript data.
     * @param path Real path of a file
     */
    public void javascript(String path) {
        this.out.println("HTTP/1.1 304 OK");
        this.out.println("Content-Type: text/javascript");
        plainText(path);
    }

    /**
     * Send back CSS data header.
     * @param path Real path of a file in computer.
     */
    public void css(String path) {
        this.out.println("HTTP/1.1 304 OK");
        this.out.println("Content-Type: text/css");
        plainText(path);
    }

    /**
     * Get request every 100 miliseconds.
     */
    @Override
    public void run() {
        try {
            while (this.running) {
                processRequest();
                Thread.sleep(100);
            }
            this.sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}