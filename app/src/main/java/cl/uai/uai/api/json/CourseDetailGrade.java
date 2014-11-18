package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 18-11-14.
 */
public class CourseDetailGrade {

    @Key
    public String date;

    @Key
    public String name;

    @Key
    public String grade;

    public String getDateNoTime() {
        return date.split(" ")[0];
    }

}
