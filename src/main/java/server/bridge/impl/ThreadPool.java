package server.bridge.impl;

import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import javax.naming.InsufficientResourcesException;

public class ThreadPool implements ThreadInterface {

    private final int DEFAULT_NUM_THREADS = 10;
    private int numThreads;
    private int numActiveThreads = 0;
    private Set<ClientThread> activeThreads;
    private Queue<ClientThread> waitingThreads;
    private Queue<ClientThreadInput> threadLine;

    public ThreadPool() {
        this.numThreads = DEFAULT_NUM_THREADS;
        this.activeThreads = new HashSet<ClientThread>();
        this.waitingThreads = new LinkedList<ClientThread>();
        this.threadLine = new LinkedList<ClientThreadInput>();
        this.initializeThreads();
    }

    public ThreadPool(int numThreads) {
        this.numThreads = numThreads;
        this.initializeThreads();
    }
    
    private void initializeThreads() {
        for (int i = 0; i < this.numThreads; i++) {
            this.waitingThreads.add(new ClientThread());
        }
    }

    @Override
    public void allocateClientThread(Socket socket, Runnable callback) {
        ClientThread clientThread;
        try {
            clientThread = this.waitingThreads.remove();
        } catch (NoSuchElementException e) {
            this.threadLine.add(new ClientThreadInput(socket, callback));
            return;
        }

        startThread(clientThread, socket, callback);
    }

    public int getNumActiveThreads() {
        return numActiveThreads;
    }

    private void startThread(ClientThread clientThread, Socket socket, Runnable callback) {
        numActiveThreads++;
        this.activeThreads.add(clientThread);
        clientThread.setSocket(socket).setCallback(() -> {
            this.freeThread(clientThread);
            new Thread(callback).start();
        }).start();
    }

    private void freeThread(ClientThread clientThread) {
        ClientThreadInput clientThreadInput = this.threadLine.poll();
        if (clientThreadInput == null) {
            this.numActiveThreads--;
            this.activeThreads.remove(clientThread);
            this.waitingThreads.add(clientThread);
        } else {
            startThread(clientThread, clientThreadInput.socket, clientThreadInput.callback);
        }
    }

    

    private class ClientThreadInput {

        public Socket socket;
        public Runnable callback;

        public ClientThreadInput(Socket socket, Runnable callback) {
            this.socket = socket;
            this.callback = callback;
        }

    }

}
