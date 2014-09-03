package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.CoursesIndexResponse;
import cl.uai.uai.api.json.CoursesPeriod;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.MessagesParser;
import cl.uai.uai.api.json.MessagesResponse;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class CoursesIndexRequest extends GoogleHttpClientSpiceRequest<CoursesPeriod[]> {

    public CoursesIndexRequest() {
        super(CoursesPeriod[].class);
    }

    @Override
    public CoursesPeriod[] loadDataFromNetwork() throws Exception {

        String url = String.format("http://webapi.uai.cl/inetmobile/courses?token=1243");

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        CoursesPeriod[] periods = request.execute().parseAs(CoursesIndexResponse.class).courses;
        return periods;
    }

    public String createCacheKey() {
        return "courses_index";
    }

}