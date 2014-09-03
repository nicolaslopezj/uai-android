package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class Message implements Serializable {

    @Key
    public String id;

    @Key
    public String body;

    @Key
    public String from;

}
