package server.factory;

import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

public class HTTPServerFactory {

    private static HashMap<Integer, HTTPServer> portToServerMap = new HashMap<>();
    public static enum ServerType {THREAD_POOL}

    public static HTTPServer createServer(int port, ServerType serverType) throws IllegalArgumentException {
        if (portToServerMap.containsKey(port)) {
            throw new IllegalArgumentException(String.format("Server already exists at port %d", port));
        } else {
            HTTPServer httpServer;
            switch (serverType) {
                case THREAD_POOL:
                    httpServer = new HTTPServerThreadPool(port);
                    break;
                default:
                    throw new IllegalArgumentException("If this is reached, all hell breaks loose");
            }

            return httpServer;
        }
    }

    public static HTTPServer getServer(int port) {
        return portToServerMap.get(port);
    }

}
