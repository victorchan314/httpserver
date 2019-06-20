package server.server.impl;

import javax.naming.InsufficientResourcesException;

public class HTTPServerThreadPool extends HTTPServer {

    HTTPServerThreadPool(int port) {
        super(port, new ThreadPool());
    }

    @Override
    protected void handleInsufficientResourcesException(InsufficientResourcesException e) {
        System.err.println(e.getMessage());
    }

}
