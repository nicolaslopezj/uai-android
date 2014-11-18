package cl.uai.uai.api;

import android.net.Uri;
import android.util.Log;

import java.net.URLEncoder;

import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class ApiUrl {

    public static String getBaseUrl() {
        return "http://webapitest.uai.cl/inetmobile/";
    }

    public static String getUrlForService(String service, String query) {
        String url = Uri.parse(getBaseUrl() + service)
                .buildUpon()
                .appendQueryParameter("token", Helper.getToken())
                .build().toString();
        if (!query.equals("")) {
            url += "&" + query;
        }
        Log.i("URL Request", url);
        return url;
    }

    public static String getUrlForService(String service) {
        return getUrlForService(service, "");
    }

    public static String getLoginUrl() {
        return ApiUrl.getBaseUrl() + "login";
    }

    public static String getNicoBaseUrl() {
        return "https://uai.lopezjullian.com/api/";
    }

    public static String getNicoUrlForService(String service, String query) {
        return getNicoBaseUrl() + service + "?app_id=3&app_secret=67lUnzMWfOJuWa8FH7UdhcJMVSTFDmXB&" + query;
    }

    public static String getNicoUrlForService(String service) {
        return getNicoUrlForService(service, "");
    }

}
