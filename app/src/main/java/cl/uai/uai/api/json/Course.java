package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;

import cl.uai.uai.main.Helper;
import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class Course implements Serializable {

    @Key
    public Integer id;

    @Key
    protected String name;

    public String getName() {
        return StringsHelper.makeItBeautiful(name);
    }

}
