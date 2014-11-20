package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.CalendarEvent;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class CalendarRequest extends GoogleHttpClientSpiceRequest<CalendarEvent[]> {

    public int type;
    public int PERSONAL_CALENDAR = 1;
    public int PUBLIC_CALENDAR = 2;

    public CalendarRequest(int _type) {
        super(CalendarEvent[].class);
        type = _type;
    }

    @Override
    public CalendarEvent[] loadDataFromNetwork() throws Exception {
        String url = ApiUrl.getUrlForService(type == PERSONAL_CALENDAR ? "JSONPersonal_Calendar" : "JSONevent_calendar");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(CalendarEvent[].class);
    }

    public String createCacheKey() {
        return "calendar_" + type;
    }

}