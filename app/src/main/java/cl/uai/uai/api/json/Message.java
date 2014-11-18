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

    public String getBeautifulFrom() {
        String _from = from;
        _from = _from.replace("_", " ");

        _from = _from.substring(0, 1).toUpperCase()
                + _from.substring(1, _from.length());

        _from = _from.replace("uai", "UAI");

        return _from;
    }

}
