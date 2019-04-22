package comp6331.assignment2.tcp;// Java client program for ANU's comp3310 sockets Lab
// Peter Strazdins, RSCS ANU, 03/18

import java.io.*;
import java.net.*;

public class TcpClient {
    private final Socket socket;

    public TcpClient(String host, int port) throws IOException{
        this.socket = new Socket(InetAddress.getByName(host), port);
    }

    public void send(String message) throws IOException{
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        out.writeUTF(message);
    }

    public String receive() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String result = "";
        String t;
        while((t = br.readLine()) != null) {
            result += t;
        }
        br.close();
        return result;
    }

    public void close() throws IOException{
        this.socket.close();
    }

    public Socket getSocket(){
        return this.socket;
    }

}
