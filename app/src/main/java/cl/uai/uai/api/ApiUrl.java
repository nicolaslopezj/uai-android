package cl.uai.uai.api;

import android.net.Uri;

import java.net.URLEncoder;

import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class ApiUrl {

    public static String getBaseUrl() {
        return "http://webapi.uai.cl/inetmobile/";
    }

    public static String getUrlForService(String service, String query) {
        return getBaseUrl() + service + "?token=" + Helper.getToken() + "&" + query;
    }

    public static String getUrlForService(String service) {
        return getUrlForService(service, "");
    }

    public static String getLoginUrl(String username, String password) {
        return ApiUrl.getBaseUrl() + "login?user_id=" + Uri.encode(username) + "&password=" + Uri.encode(password) + "&client_token=1243";
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
