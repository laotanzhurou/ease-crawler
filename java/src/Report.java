import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Report {

    int urlCount;
    int htmlPageCount;
    int nonHtmlObjectCount;
    int largestPageSize;
    String largestPageUrl;
    int smallestPageSize = Integer.MAX_VALUE;
    String smallestPageUrl;
    String oldestPage;
    Date oldestPageModifiedTime = new Date();
    String newestPage;
    Date newestPageModifiedTime = new Date(0L);
    int invalidUrlsCount;
    Map<String, String> redirects = new HashMap<>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public void process(String url, HttpResponse response) throws Exception{
        urlCount++;
        if(response.getCode() == 200){ //200
            int pageSize = Integer.parseInt(response.getContentLength());
            Date lastModifiedDate = dateFormat.parse(response.getLastModified());

            if(response.getContentType().equals("text/html"))
                htmlPageCount++;
            else
                nonHtmlObjectCount++;
            if(pageSize > largestPageSize){
                largestPageSize = pageSize;
                largestPageUrl = url;
            }
            if(pageSize < smallestPageSize){
                smallestPageSize = pageSize;
                smallestPageUrl = url;
            }
            if(lastModifiedDate.before(oldestPageModifiedTime)){
                oldestPageModifiedTime = lastModifiedDate;
                oldestPage = url;
            }
            if(lastModifiedDate.after(newestPageModifiedTime)){
                newestPageModifiedTime = lastModifiedDate;
                newestPage = url;
            }
        }else if(response.getCode() > 300 && response.getCode() < 400){ //30x
            redirects.put(url, response.getLocation());
        }else if(response.getCode() > 400 && response.getCode() < 500){//40x
            invalidUrlsCount++;
        }

    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("redirects: \n");
        redirects.forEach((f, t) -> builder.append("from: " + f + "\t" + "to: " + t + "\n"));
        return  "Analytics Report: \n"
                + "urlCount: " + urlCount + "\r\n"
                + "htmlPageCount: " + htmlPageCount + "\r\n"
                + "nonHtmlObjectCount: " + nonHtmlObjectCount + "\r\n"
                + "largestPageSize: " + largestPageSize + "\r\n"
                + "largestPageUrl: " + largestPageUrl + "\r\n"
                + "smallestPageSize: " + smallestPageSize + "\r\n"
                + "smallestPageUrl: " + smallestPageUrl + "\r\n"
                + "oldestPage: " + oldestPage + "\r\n"
                + "oldestPageModifiedTime: " + oldestPageModifiedTime + "\r\n"
                + "newestPage: " + newestPage + "\r\n"
                + "newestPageModifiedTime: " + newestPageModifiedTime + "\r\n"
                + "invalidUrlsCount: " + invalidUrlsCount + "\r\n"
                + builder.toString();
    }

}
