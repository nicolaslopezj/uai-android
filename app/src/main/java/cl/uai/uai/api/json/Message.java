package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;

import cl.uai.uai.main.Helper;
import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class Message implements Serializable {

    @Key
    public String id;

    @Key
    public String body;

    @Key
    protected String from;

    @Key
    public String date;

    public String getFrom() {
        String _from = from;
        _from = _from.replace("_", " ");
        return StringsHelper.makeItBeautiful(_from);
    }

    public boolean isReaded() {
        return Helper.isMessageReaded(id);
    }

    public void markAsRead() {
        Helper.setMessageReaded(id);
    }

}
