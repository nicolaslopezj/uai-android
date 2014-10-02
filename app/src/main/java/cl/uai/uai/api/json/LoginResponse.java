package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class LoginResponse {

    @Key
    public Boolean success;

    @Key
    public String token;

}
