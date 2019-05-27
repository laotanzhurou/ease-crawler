package org.laotanzhurou.tcp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Author: Quan Ke
 */
public class TcpClient {
    private final Socket socket;

    public TcpClient(String host, int port) throws IOException{
        this.socket = new Socket(InetAddress.getByName(host), port);
    }

    public void send(String message) throws IOException{
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        out.writeUTF(message);
    }

    /*
        reads from socket and close tcp connection
     */
    public List<String> receive() throws IOException{
        List<String> content = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String t;
        while((t = br.readLine()) != null) {
            content.add(t);
        }
        br.close();
        return content;
    }

    public void close() throws IOException{
        this.socket.close();
    }

    public Socket getSocket(){
        return this.socket;
    }

}
