package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 19-08-14.
 */
public class Event {

    @Key
    public Integer id;

    @Key
    public String date;

    @Key
    public String room;

    @Key
    public String name;

    @Key
    public Integer section;

    @Key
    public Boolean suspended;

    @Key
    public String teacher;

    @Key
    public String type;
}
