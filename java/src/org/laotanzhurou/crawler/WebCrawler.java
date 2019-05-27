package org.laotanzhurou.crawler;

import org.laotanzhurou.http.HttpClient;
import org.laotanzhurou.http.HttpResponse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebCrawler {

    private static final String DEFAULT_URL = "www.google.com";

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
        while (!queue.isEmpty()) {
            String target = queue.pollFirst();

            System.out.println("URL: " + target);

            String host = Parser.parseHost(target, defaultHost);
            int port = Parser.parsePort(target, defaultPort);
            String path = Parser.parsePath(target);

            try {
                HttpClient client = new HttpClient();
                Thread.sleep(2000);

                List<String> content = client
                        .get(host, port)
                        .path(path)
                        .build();
                HttpResponse response = HttpResponse.parse(content);
                results.put(target, response);

                System.out.println("Status: " + response.getCode());
                report.process(target, response);

                Parser.parseHrefLinks(response.getEntity(), host, port).stream().forEach(uri -> {
                    if (!results.containsKey(uri))
                        queue.addFirst(uri);
                });
                Parser.parseSrcLinks(response.getEntity(), host, port, Parser.ancestorPath(path)).stream().forEach(uri -> {
                    if (!results.containsKey(uri))
                        queue.addFirst(uri);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("");
        System.out.println(report);
    }

}
