package server.threads;

import server.util.HTTPResponse;

import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Socket socket;
    private Runnable callback;
    private Thread thread;

    public ClientThread setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    public ClientThread setCallback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
        this.thread = thread;
    }

    @Override
    public void run() {
        if (this.socket == null) {
            System.err.println("Socket must be initalized before a connection can be established");
            return;
        }

        HTTPResponse httpResponse = new HTTPResponse(this.socket);
        httpResponse.send("8888");
        httpResponse.close();
        try {
            this.socket.close();
        } catch (IOException e) {
            System.err.println(String.format("Error closing client socket: %s", e.getMessage()));
        }

        try {
            this.thread.sleep(10000);
        } catch (InterruptedException e) {
            System.err.println(String.format("Thread interrupted when it should be: %s", e.getMessage()));
        }

        if (this.callback != null) {
            new Thread(this.callback).start();
        }
    }

}
