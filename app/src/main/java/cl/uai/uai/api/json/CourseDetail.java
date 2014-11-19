package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 18-11-14.
 */
public class CourseDetail {

    @Key
    public String final_grade;

    @Key
    public String final_status;

    @Key
    public String announcement;

    @Key
    public CourseDetailTeacher[] led_by;

    @Key
    public CourseDetailGrade[] grades;

    public float getFinalGradeFloat() {
        String grade = final_grade.replace(",", ".");
        return Float.parseFloat(grade);
    }

}
