package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Message;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class MessagesIndexRequest  extends GoogleHttpClientSpiceRequest<Message[]> {

    public MessagesIndexRequest() {
        super(Message[].class);
    }

    @Override
    public Message[] loadDataFromNetwork() throws Exception {

        String url = String.format("http://nicolaslopez.co/uploads/files/messages.json");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(Message[].class);
    }

    public String createCacheKey() {
        return "messages";
    }

}