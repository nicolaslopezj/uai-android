package cl.uai.uai.api.json;

import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class CalendarEvent {

    @Key
    public String UID;

    @Key
    public String Start;

    @Key
    public String End;

    @Key
    public String Summary;

    @Key
    public String Description;

    @Key
    public String Organizer;

    @Key
    public String CatType;

    public boolean hasSection() {
        return Description.matches(".+?(?i)sec.+?");
    }

    public String getBeatifulName() {
        return getName() + "\n" + getRoom();
    }

    public String getName() {
        if (hasSection()) {
            Pattern p = Pattern.compile("(.+)(?i)sec");
            Matcher m = p.matcher(Description);
            if(m.find()) {
                return m.group(0).replace("SEC", "").replace("sec", "").trim();
            }
            return Description;
        } else {
            return Description;
        }
    }

    public String getRoom() {
        Pattern p = Pattern.compile("[0-9]?[0-9]?[0-9]-[a-zA-Z]");
        Matcher m = p.matcher(Description);
        if(m.find()) {
            return m.group(0).trim();
        }
        return "";
    }

}
