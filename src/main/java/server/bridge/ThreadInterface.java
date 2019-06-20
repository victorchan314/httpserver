package server.bridge;

import java.net.Socket;
import javax.naming.InsufficientResourcesException;

interface ThreadInterface {

    void allocateClientThread(Socket socket, Runnable callback) throws InsufficientResourcesException;

}
