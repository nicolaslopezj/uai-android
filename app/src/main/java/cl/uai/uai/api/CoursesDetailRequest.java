package cl.uai.uai.api;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import cl.uai.uai.api.json.CourseDetail;

/**
 * Created by nicolaslopezj on 18-11-14.
 */
public class CoursesDetailRequest extends GoogleHttpClientSpiceRequest<CourseDetail> {

    private Integer id;

    public CoursesDetailRequest(Integer _id) {
        super(CourseDetail.class);
        id = _id;
    }

    @Override
    public CourseDetail loadDataFromNetwork() throws Exception {

        String url = ApiUrl.getUrlForService("course_result", "course_id=" + id);

        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setParser(new JacksonFactory().createJsonObjectParser());

        return request.execute().parseAs(CourseDetail.class);
    }

    public String createCacheKey() {
        return "course-detail-" + id;
    }

}
