package cl.uai.uai.api;

import android.net.Uri;
import android.util.Log;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.LoginResponse;
import cl.uai.uai.api.json.Success;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class LoginRequest extends GoogleHttpClientSpiceRequest<LoginResponse> {

    //cancellation o reservation
    public String username;
    public String password;

    public LoginRequest(String _username, String _password) {
        super(LoginResponse.class);
        username = _username;
        password = _password;
    }

    @Override
    public LoginResponse loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getLoginUrl(username, password);

        String data = "{ \"user_id\": \"" + username + "\",\"password\": \"" + password + "\", \"client_token\": \"98f8095442e71a3f4f6b0c2f804fb2fc\" }";
        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(url), ByteArrayContent.fromString("application/json", data));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        LoginResponse response = request.execute().parseAs(LoginResponse.class);
        return response;
    }

    public String createCacheKey() {
        return "login_" + username + "_" + password;
    }

}