package cl.uai.uai.api;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.util.Arrays;

import cl.uai.uai.api.json.Sport;
import cl.uai.uai.api.json.SportArray;

/**
 * Created by nicolaslopezj on 21-08-14.
 */
public class SportsIndexRequest extends GoogleHttpClientSpiceRequest<Sport[]> {

    public SportsIndexRequest() {
        super(Sport[].class);
    }

    @Override
    public Sport[] loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getUrlForService("sport_slots");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        SportArray sportsArray = request.execute().parseAs(SportArray.class);
        return sportsArray.sport_slots;
    }

    public String createCacheKey() {
        return "sports";
    }

}