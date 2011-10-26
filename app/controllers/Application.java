package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

public class Application extends Controller {

    public static class Result {
        public Result(String contents) {
            this.contents = contents;
        }

        public final String contents;
    }

    public static void index() {
        render();
    }

    public static void get(String url, final String callback) {
            F.Promise<WS.HttpResponse> remoteCall = WS.url(url).getAsync();

        await(remoteCall, new F.Action<WS.HttpResponse>() {
            public void invoke(WS.HttpResponse result) {
                String replace = result.getString().replace("/", "\\/");

                String responseStr = new GsonBuilder().disableHtmlEscaping().create().toJson(new Result(replace));

                // http://blog.altosresearch.com/supporting-the-jsonp-callback-protocol-with-jquery-and-java/
                if ( callback != null ) {
                    response.contentType = "application/x-javascript";
                    responseStr = callback + "(" + responseStr + ");";
                } else {
                    response.contentType = "application/json";
                }

                renderJSON(responseStr);
            }
        });
    }

    public static String sanitizeJsonpParam(String s) {
        if (StringUtils.isEmpty(s)) return null;
        if (!StringUtils.startsWithIgnoreCase(s, "jsonp")) return null;
        if (StringUtils.length(s) > 128) return null;
        if (!s.matches("^jsonp\\d+$")) return null;
        return s;
    }
}