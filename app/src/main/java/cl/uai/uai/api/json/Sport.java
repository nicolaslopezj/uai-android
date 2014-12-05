package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 21-08-14.
 */
public class Sport implements Serializable {

    @Key
    public Integer id;

    @Key
    public Integer available;

    @Key
    public Integer capacity;

    @Key
    public String led_by;

    @Key
    public String location;

    @Key
    public String name;

    @Key
    public Boolean reserved;

    @Key
    public String rsvp_until;

    @Key
    public String when;

    public String getWhen() {
        DateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(StringsHelper.stringToDate(when, "yyyy-MM-dd'T'HH:mm:ss"));
    }

}
