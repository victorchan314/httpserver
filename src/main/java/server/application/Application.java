package server.application;

import server.factory.HTTPServerFactory;
import server.server.HTTPServer;

public class Application {

    public static void main(String[] args) {
        //HTTPServer httpServer = new HTTPServerThreadPool(8888);
        HTTPServer httpServer = HTTPServerFactory.createServer(8888, HTTPServerFactory.ServerType.THREAD_POOL);
    }

}
