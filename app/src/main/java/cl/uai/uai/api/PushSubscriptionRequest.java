package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Success;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class PushSubscriptionRequest extends GoogleHttpClientSpiceRequest<Success> {

    //cancellation o reservation
    public String regid;

    public PushSubscriptionRequest(String _regid) {
        super(Success.class);
        regid = _regid;
    }

    @Override
    public Success loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getUrlForService("push_subscription", "os=android&push_device_token=" + regid);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(Success.class);
    }

    public String createCacheKey() {
        return "register_device_" + regid;
    }

}
