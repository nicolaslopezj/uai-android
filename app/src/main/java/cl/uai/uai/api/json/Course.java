package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class Course implements Serializable {

    @Key
    public Integer id;

    @Key
    public String name;

}
