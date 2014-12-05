package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.MessagesParser;
import cl.uai.uai.api.json.MessagesResponse;

/**
 * Created by nicolaslopezj on 04-12-14.
 */
public class MessagesShowRequest extends GoogleHttpClientSpiceRequest<Message> {

    public String messageId;

    public MessagesShowRequest(String _messageId) {
        super(Message.class);
        messageId = _messageId;
    }

    @Override
    public Message loadDataFromNetwork() throws Exception {
        String url = ApiUrl.getUrlForService("smessage/" + messageId);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(Message.class);
    }

    public String createCacheKey() {
        return "message_" + messageId;
    }

}