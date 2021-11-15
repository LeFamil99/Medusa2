package ca.qc.bdeb.inf203.tp2Online;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomSocket extends Socket {
    private int id;

    public CustomSocket(String host, int port, int id) throws IOException {
        super(host, port);
        this.id = id;
    }

    public CustomSocket(InetAddress address, int port, int id) throws IOException {
        super(address, port);
        this.id = id;
    }
}
