import java.util.*;
import java.util.stream.Collectors;

public class HttpResponse {

    private static String HEADER_ANCHOR = ":";

    private int code;
    private Map<HttpHeader, String> headers = new HashMap<>();
    private String entity;

    public static HttpResponse parse(List<String> rawContent){
        HttpResponse response = new HttpResponse();

        StringBuilder entity = new StringBuilder();

        boolean startOfEntity = false;
        for(int i=0;i<rawContent.size();i++){
            String contentLine = rawContent.get(i);
            //http status line
            if(i==0){
                parseCode(response, contentLine);
                continue;
            }
            //header
            if(!startOfEntity){
                if(contentLine.isEmpty()){
                    startOfEntity = true;
                }else{
                    parseHeader(response, contentLine);
                }
                continue;
            }
            //content
            entity.append(contentLine);
        }
        response.entity = entity.toString();
        return response;
    }

    private static void parseHeader(HttpResponse response, String line){
        int anchorIndex = line.indexOf(HEADER_ANCHOR);
        String hKey = line.substring(0, anchorIndex);
        HttpHeader h = HttpHeader.headers.get(hKey);
        if(h!=null){
            String content = line.substring(anchorIndex+2, line.length()).trim();
            response.headers.put(h, content);
        }
    }

    private static void parseCode(HttpResponse response, String line){
        String[] fields = line.split(" ");
        response.setCode(Integer.parseInt(fields[1]));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContentType() {
        return headers.get(HttpHeader.ContentType);
    }

    public String getContentLength() {
        return headers.get(HttpHeader.ContentLength);
    }

    public String getLastModified() {
        return headers.get(HttpHeader.LastModified);
    }

    public String getLocation(){
        return headers.get(HttpHeader.Location);
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public enum HttpHeader{

        Date("Date"),
        Server("Server"),
        LastModified("Last-Modified"),
        AcceptRanges("Accept-Ranges"),
        ContentLength("Content-Length"),
        ContentType("Content-Type"),
        Location("Location");

        final static Map<String, HttpHeader> headers;

        static{
            headers = Arrays.stream(HttpHeader.values()).collect(Collectors.toMap(h -> h.value, h -> h));
        }

        private String value;

        HttpHeader(String value){
            this.value = value;
        }

        public Map<String, HttpHeader> headers(){
            return headers;
        }

    }

}
