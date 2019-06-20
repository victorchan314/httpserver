package server.server.impl;

import server.bridge.impl.ThreadPool;
import server.server.HTTPServer;

import javax.naming.InsufficientResourcesException;

public class HTTPServerThreadPool extends HTTPServer {

    public HTTPServerThreadPool(int port) {
        super(port, new ThreadPool());
    }

    @Override
    protected void handleInsufficientResourcesException(InsufficientResourcesException e) {
        System.err.println(e.getMessage());
    }

}
