package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Sport;
import cl.uai.uai.api.json.SportArray;
import cl.uai.uai.api.json.Success;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class SportActionRequest extends GoogleHttpClientSpiceRequest<Success> {

    //cancellation o reservation
    public String action;
    public Integer sport_id;

    public SportActionRequest(String _action, Integer _sport_id) {
        super(Success.class);
        action = _action;
        sport_id = _sport_id;
    }

    @Override
    public Success loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getUrlForService("sport_" + action, "slot_id=" + sport_id);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(Success.class);
    }

    public String createCacheKey() {
        return "sports" + action + sport_id;
    }

}