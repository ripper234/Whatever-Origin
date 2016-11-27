package controllers;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

public class Application extends Controller {

    public static class Result {
        public Result(String contents, Status status) {
            this.contents = contents;
            this.status = status;
        }

        public final String contents;
        public final Status status;

        // "status":{"url":"https:\/\/mtgox.com\/api\/0\/data\/ticker.php","content_type":"application\/json","http_code":200

        public static class Status {
            public final String url;
            public final String content_type;
            public final int http_code;

            public Status(String url, String content_type, int http_code) {
                this.url = url;
                this.content_type = content_type;
                this.http_code = http_code;
            }
        }
    }

    public static void index() {
        render();
    }

    public static void get(String url, final String callback) {
        F.Promise<WS.HttpResponse> remoteCall = WS.url(url).getAsync();

        await(remoteCall, new F.Action<WS.HttpResponse>() {
            public void invoke(WS.HttpResponse result) {
                String responseStr = getResponseStr(result, encodedUrl);

                // http://blog.altosresearch.com/supporting-the-jsonp-callback-protocol-with-jquery-and-java/
                if ( callback != null ) {
                    response.contentType = "application/x-javascript";
                    responseStr = callback + "(" + responseStr + ")";
                } else {
                    response.contentType = "application/json";
                }

                renderJSON(responseStr);
            }
        });
    }

    private static String getResponseStr(WS.HttpResponse result, String url) {
        // Handle raw json to prevent it from being escaped and encoded as a string
        if (result.getContentType().equals("application/json")) {
            return "{\"contents\":" + result.getString() + "}";
        }

        String responseStr =    result.getString();

        responseStr = new GsonBuilder().disableHtmlEscaping().create().toJson(new Result(responseStr , new Result.Status(url, result.getContentType(), result.getStatus())));
        return responseStr;
    }

    public static String sanitizeJsonpParam(String s) {
        if (StringUtils.isEmpty(s)) return null;
        if (!StringUtils.startsWithIgnoreCase(s, "jsonp")) return null;
        if (StringUtils.length(s) > 128) return null;
        if (!s.matches("^jsonp\\d+$")) return null;
        return s;
    }
}
