package server.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;
import javax.naming.InsufficientResourcesException;

abstract class HTTPServer {

    private int port;
    private ServerSocket serverSocket;
    private AtomicLong numConnections = new AtomicLong();
    private ThreadInterface threadInterface;

    protected HTTPServer(int port, ThreadInterface threadInterface) {
        this.port = port;
        this.threadInterface = threadInterface;

        try {
            this.serverSocket = new ServerSocket(port);
            serveForever();
        } catch (IOException e) {
            System.err.println(String.format("Error creating server socket: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            System.err.println(String.format("Illegal port %d; port must be between %d and %d", port, 0, 65535));
        }
    }

    private void serveForever() {
        Socket clientSocket;

        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
                this.threadInterface.allocateClientThread(clientSocket, this::decrementNumConnections);
                this.numConnections.incrementAndGet();
                System.out.println(String.format("Number of connections: %s", this.numConnections));
            } catch (IOException e) {
                System.err.println(String.format("Client connection error: %s", e.getMessage()));
            } catch (InsufficientResourcesException e) {
                this.handleInsufficientResourcesException(e);
            }
        }
    }

    private void decrementNumConnections() {
        this.numConnections.decrementAndGet();
    }

    protected abstract void handleInsufficientResourcesException(InsufficientResourcesException e);

}
