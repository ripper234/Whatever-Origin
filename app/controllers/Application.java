package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public static void get(String url) {
        F.Promise<WS.HttpResponse> remoteCall = WS.url(url).getAsync();

        await(remoteCall, new F.Action<WS.HttpResponse>() {
            public void invoke(WS.HttpResponse result) {
                String json = new GsonBuilder().disableHtmlEscaping().create().toJson(result.getString());
                response.contentType = "application/javascript";
                render(json);
            }
        });
    }
}