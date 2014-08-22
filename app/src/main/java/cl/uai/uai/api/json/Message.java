package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class Message {

    @Key
    public Integer id;

    @Key
    public String body;

    @Key
    public String from;

}
