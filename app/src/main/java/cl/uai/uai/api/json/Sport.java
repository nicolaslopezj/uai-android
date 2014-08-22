package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;

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
    public Boolean reserverd;

    @Key
    public String rsvp_until;

    @Key
    public String when;

}
