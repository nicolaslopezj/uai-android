package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class HomeImagesRequest extends GoogleHttpClientSpiceRequest<String[]> {

    public HomeImagesRequest() {
        super(String[].class);
    }

    @Override
    public String[] loadDataFromNetwork() throws Exception {

        String url = String.format("http://webapi.uai.cl/inetmobile/home_images?token=sdasd");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(String[].class);
    }

    public String createCacheKey() {
        return "home-images";
    }

}
