package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cl.uai.uai.main.Helper;
import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class Message implements Serializable {

    @Key
    public String id;

    @Key
    public String title;

    @Key
    public String body;

    @Key
    protected String from;

    @Key
    public String date;

    @Key
    public boolean deleted;

    @Key
    public boolean readed;

    public String getDateParsed() {
        DateFormat format = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        return format.format(StringsHelper.stringToDate(date, "yyyy-MM-dd'T'HH:mm:ss"));
    }

    public String getBodyWithoutHtmlTags() {
        return StringsHelper.stripHtmlTags(body);
    }

    public String getFrom() {
        String _from = from;
        _from = _from.replace("_", " ");
        return StringsHelper.makeItBeautiful(_from);
    }

}
