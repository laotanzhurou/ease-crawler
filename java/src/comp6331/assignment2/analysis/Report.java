package comp6331.assignment2.analysis;

import java.util.Date;
import java.util.Map;

public class Report {

    int urlCount;
    int htmlPageCount;
    int nonHtmlObjectCount;
    int largestPageSize;
    String largestPageUrl;
    int smallestPageSize;
    String smallestPageUrl;
    String oldestPage;
    Date oldestPageModifiedTime;
    String newestPage;
    Date newestPageModifiedTime;
    int invalidUrlsCount;
    Map<String, String> redirects;

    public void process(String url, String content){
    }

}
