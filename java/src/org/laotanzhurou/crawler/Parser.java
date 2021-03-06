package org.laotanzhurou.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 *  Author: Quan Ke
 */
public class Parser {

    private static final String PROTOCAL =  "http://";

    private static final String HREF_ANCHOR = "<a href=\"";
    private static final char HREF_END = '\"';
    private static final String SRC_ANCHOR = "<img src=\"";
    private static final char SRC_END = '\"';


    private static final char PORT_ANCHOR = ':';
    private static final char PORT_END = '/';

    public static List<String> parseHrefLinks(String input, String defaultHost, int defaultPort){
        List<String> links = new ArrayList<>();
        int p = input.indexOf(HREF_ANCHOR);
        while(p!=-1){
            int linkStart = p + HREF_ANCHOR.length();
            int linkEnd = linkStart;
            boolean q = true;
            while(q){
                if(input.charAt(linkEnd)!=HREF_END)
                    linkEnd++;
                else
                    q = false;
            }
            String url = input.substring(linkStart, linkEnd);
            if(!url.contains(PROTOCAL))
                url = PROTOCAL + defaultHost + PORT_ANCHOR + defaultPort + (url.equals("/")?"":PORT_END) + url;
            links.add(url);
            p = input.indexOf(HREF_ANCHOR, linkEnd);
        }
        return links;
    }

    public static List<String> parseSrcLinks(String input, String defaultHost, int defaultPort, String defaultPath){
        List<String> links = new ArrayList<>();
        int p = input.indexOf(SRC_ANCHOR);
        while(p!=-1){
            int linkStart = p + SRC_ANCHOR.length();
            int linkEnd = linkStart;
            boolean q = true;
            while(q){
                if(input.charAt(linkEnd)!=SRC_END)
                    linkEnd++;
                else
                    q = false;
            }
            String url = input.substring(linkStart, linkEnd);
            if(!url.contains(PROTOCAL))
                url = PROTOCAL + defaultHost + PORT_ANCHOR + defaultPort + defaultPath + url;
            links.add(url);
            p = input.indexOf(HREF_ANCHOR, linkEnd);
        }
        return links;
    }

    public static String parseHost(String url, String defaultHost){
        if(url.contains(PROTOCAL)){
            url = url.split(PROTOCAL)[1];
            int end = url.indexOf(PORT_ANCHOR);
            if(end == -1)
                end = url.indexOf(PORT_END);
            return url.substring(0, end);
        }
        return defaultHost;
    }

    public static int parsePort(String url, int defaultPort){
        if(url.contains(PROTOCAL)) {
            url = url.split(PROTOCAL)[1];
            int start = url.indexOf(PORT_ANCHOR);
            if(start < 0)
                return defaultPort;
            else{
                start++;
                int end = url.indexOf(PORT_END, start);
                return Integer.parseInt(url.substring(start, end));
            }

        }
        return defaultPort;
    }

    public static String parsePath(String url){
        if(url.contains(PROTOCAL)){
            url = url.split(PROTOCAL)[1];
            int start = url.indexOf(PORT_END);
            if(start < 0)
                return "";
            else{
                return url.substring(start, url.length());
            }
        }
        return url;
    }

    public static String ancestorPath(String path){
        int end = path.lastIndexOf("/");
        return path.substring(0, end+1);
    }
}
