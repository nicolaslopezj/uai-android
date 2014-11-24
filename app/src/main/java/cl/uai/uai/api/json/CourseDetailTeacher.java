package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import cl.uai.uai.main.Helper;
import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 18-11-14.
 */
public class CourseDetailTeacher {

    @Key
    protected String role;

    @Key
    protected String name;

    @Key
    protected String email;

    public String getRole() {
        return StringsHelper.makeItBeautiful(role);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getFirstName() {
        String[] parts = name.split(",");
        return StringsHelper.makeItBeautiful(parts[parts.length - 1]).replace(".", "").trim();
    }

    public String getLastName() {
        String[] parts = name.split(",");
        return StringsHelper.makeItBeautiful(parts[0]).replace(".", "").trim();
    }

    public String getEmail() {
        return StringsHelper.lowercase(email);
    }

}
