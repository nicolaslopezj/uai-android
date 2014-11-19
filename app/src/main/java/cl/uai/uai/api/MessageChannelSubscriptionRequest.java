package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.MessagesParser;
import cl.uai.uai.api.json.MessagesResponse;
import cl.uai.uai.api.json.Success;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class MessageChannelSubscriptionRequest extends GoogleHttpClientSpiceRequest<Success> {

    public String channelName;
    public boolean enabled;

    public MessageChannelSubscriptionRequest(String _channelName, boolean _enabled) {
        super(Success.class);
        channelName = _channelName;
        enabled = _enabled;
    }

    @Override
    public Success loadDataFromNetwork() throws Exception {
        String query = "channel=" + channelName + "&enabled=";
        query += enabled ? "true" : "false";
        String url = ApiUrl.getUrlForService("message_channel_subscription", query);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        Success response = request.execute().parseAs(Success.class);
        return response;
    }

    public String createCacheKey() {
        return "message_channel_subscription" + channelName;
    }

}