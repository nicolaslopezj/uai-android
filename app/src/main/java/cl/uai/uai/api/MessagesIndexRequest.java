package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.MessagesParser;
import cl.uai.uai.api.json.MessagesResponse;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class MessagesIndexRequest  extends GoogleHttpClientSpiceRequest<Message[]> {

    public MessagesIndexRequest() {
        super(Message[].class);
    }

    @Override
    public Message[] loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getUrlForService("messages", "since=12-03-2013");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        MessagesResponse response = request.execute().parseAs(MessagesResponse.class);
        Message[] messages = MessagesParser.parse(response);
        return messages;
    }

    public String createCacheKey() {
        return "messages";
    }

}