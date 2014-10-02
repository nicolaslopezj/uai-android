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

    public String getRealName() {
        String[] parts = this.name.split(" Sec. ");
        return parts[0];
    }

    public String getSection() {
        String[] parts = this.name.split(" Sec. ");
        if (parts.length > 1) {
            return parts[1];
        }
        return null;
    }

}
