package cl.uai.uai.api.json;

import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class CalendarEvent implements Serializable {

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

    public String getTeacherName() {
        Pattern p = Pattern.compile("(?i)(prof. )?(.*)");
        Matcher m = p.matcher(Organizer);
        if(m.find()) {
            return StringsHelper.makeItBeautiful(m.group(2).trim());
        }
        return StringsHelper.makeItBeautiful(Organizer);
    }

    public String getNameWithSection() {
        if (hasSection()) {
            Pattern p = Pattern.compile(".+?(?i)(sec\\. ?[0-9]+)");
            Matcher m = p.matcher(Description);
            if(m.find()) {
                return m.group(0).trim();
            }
            return getName();
        } else {
            return getName();
        }
    }

    public String getName() {
        if (hasSection()) {
            Pattern p = Pattern.compile("(.+)(?i)sec");
            Matcher m = p.matcher(Description);
            if(m.find()) {
                return StringsHelper.makeItBeautiful(m.group(0).replace("SEC", "").replace("sec", ""));
            }
            return StringsHelper.makeItBeautiful(Description);
        } else {
            return StringsHelper.makeItBeautiful(Description);
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

    public String getStartAndEndDate() {
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat form2 = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat form3 = new SimpleDateFormat("HH:mm");
        java.util.Date d1 = null;
        java.util.Date d2 = null;
        try {
            d1 = form.parse(Start);
            d2 = form.parse(End);

            String date = "";
            date += form3.format(d1);
            date += " - ";
            date += form3.format(d2);
            date += " ";
            date += form2.format(d1);

            return date;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return Start + " " + End;
    }

    public Calendar startCalendar() {
        return stringToCalendar(Start);
    }

    public Calendar endCalendar() {
        return stringToCalendar(End);
    }

    private Calendar stringToCalendar(String date) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date d1 = null;
        Calendar tdy1;
        try {
            d1 = form.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        tdy1 = Calendar.getInstance();
        tdy1.setTime(d1);
        return tdy1;
    }

}
