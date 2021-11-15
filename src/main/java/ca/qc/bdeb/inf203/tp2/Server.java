package ca.qc.bdeb.inf203.tp2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket ss;

    {
        try {
            ss = new ServerSocket(6666);

            Socket s = ss.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
