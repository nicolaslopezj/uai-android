package cl.uai.uai.api;

import android.text.format.DateFormat;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.MessagesParser;
import cl.uai.uai.api.json.MessagesResponse;
import cl.uai.uai.main.Helper;
import cl.uai.uai.main.StringsHelper;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class MessagesIndexRequest  extends GoogleHttpClientSpiceRequest<Message[]> {

    public MessagesIndexRequest() {
        super(Message[].class);
    }

    @Override
    public Message[] loadDataFromNetwork() throws Exception {
        String url = ApiUrl.getUrlForService("messages", "since=" + Helper.getLastMessageRequestDate());

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        MessagesResponse response = request.execute().parseAs(MessagesResponse.class);
        MessagesParser.parse(response);

        Message[] messages = Helper.getMessagesList();

        if (messages.length > 0) {
            Message lastMessage = messages[0];
            Calendar date = StringsHelper.stringToCalendar(lastMessage.date, "yyyy-MM-dd'T'HH:mm:ss");
            date.add(Calendar.SECOND, 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Helper.setLastMessagesRequestDate(dateFormat.format(date.getTime()));
        } else {
            Helper.setLastMessagesRequestDate("");
        }

        return messages;
    }

    public String createCacheKey() {
        return "messages_since_" + Helper.getLastMessageRequestDate();
    }

}