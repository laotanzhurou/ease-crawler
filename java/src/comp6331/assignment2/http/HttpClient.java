package comp6331.assignment2.http;

import comp6331.assignment2.tcp.TcpClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class HttpClient {

    private static final String DEFAULT_PATH = "/";
    private static final String DEFAULT_PROTOCAL = "HTTP/1.0";
    private static final String CRLF = "\r\n";

    private String protocal;
    private String method;
    private String path;

    private TcpClient client;

    public HttpClient(){
        this.path = DEFAULT_PATH;
        this.protocal = DEFAULT_PROTOCAL;
    }

    public HttpClient get(String host, int port) throws Exception{
        client = new TcpClient(host, port);
        this.method = "GET";
        return this;
    }

    public HttpClient path(String path){
        this.path += path;
        return this;
    }

    public List<String> build() throws IOException{
        PrintWriter pw = new PrintWriter(client.getSocket().getOutputStream());
        pw.print(this.method + " " + this.path + " " + this.protocal + CRLF);
        pw.print(CRLF);
        pw.flush();
        List<String> response = client.receive();
        client.close();
        return response;
    }

}
