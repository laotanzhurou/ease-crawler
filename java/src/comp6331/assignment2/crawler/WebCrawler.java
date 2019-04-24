package comp6331.assignment2.crawler;

import comp6331.assignment2.analysis.Parser;
import comp6331.assignment2.analysis.Report;
import comp6331.assignment2.http.HttpClient;
import comp6331.assignment2.http.HttpResponse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler {

    private static final String DEFAULT_URL = "http://comp3310.ddns.net:7880/";

    public static void main(String[] args) throws Exception {
        //parse args
        String url;
        if (args.length == 0) {
            url = DEFAULT_URL;
        } else {
            if (args.length != 1)
                throw new RuntimeException("expect 1 argument of url, got " + args.length);
            url = args[0];
        }
        String defaultHost = Parser.parseHost(url, null);
        int defaultPort = Parser.parsePort(url, 80);

        //init
        Report report = new Report();
        LinkedList<String> queue = new LinkedList<>();
        Map<String, HttpResponse> results = new HashMap<>();
        queue.addFirst(url);

        //process
        while(!queue.isEmpty()){
            String target = queue.pollFirst();

            System.out.println("URL: " + target);

            String host = Parser.parseHost(target, defaultHost);
            int port = Parser.parsePort(target, defaultPort);
            String path = Parser.parsePath(target);

            try {
                HttpClient client = new HttpClient();
                Thread.sleep(2000);

                System.out.println("Sending Request...");
                List<String> content = client
                        .get(host, port)
                        .path(path)
                        .build();
                HttpResponse response = HttpResponse.parse(content);
                results.put(target, response);

                //TODO
                report.process(target, response);

                Parser.parseHrefLinks(response.getEntity(), host, port).stream().forEach(uri -> {
                    if(!results.containsKey(uri))
                        queue.addFirst(uri);
                });
                Parser.parseSrcLinks(response.getEntity(), host, port, Parser.ancestorPath(path)).stream().forEach(uri -> {
                    if(!results.containsKey(uri))
                        queue.addFirst(uri);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(report);
    }

}
