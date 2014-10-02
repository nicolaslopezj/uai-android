package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Event;
import cl.uai.uai.api.json.Message;

/**
 * Created by nicolaslopezj on 24-08-14.
 */
public class BusesIndexRequest extends GoogleHttpClientSpiceRequest<Event[]> {

    private String location = "santiago";

    public BusesIndexRequest(String _location) {
        super(Event[].class);
        location = _location;
    }

    @Override
    public Event[] loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getNicoUrlForService("buses/" + location);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(Event[].class);
    }

    public String createCacheKey() {
        return "events-" + location;
    }

}