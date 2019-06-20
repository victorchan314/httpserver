package server.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPResponse {

    private Socket socket;
    private PrintWriter out;

    public HTTPResponse(Socket socket) {
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(String.format("Error closing PrintWriter: %s", e.getMessage()));
        }
    }

    public void send(String s) {
        this.out.println("HTTP/1.1 200 OK");
        this.out.println("Content-type: text/html");
        this.out.println(String.format("Content-length: %d", s.length()));
        this.out.println();
        this.out.println(s);
    }

    public void close() {
        this.out.close();
    }

}
