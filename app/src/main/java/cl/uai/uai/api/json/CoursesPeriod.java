package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class CoursesPeriod {

    @Key
    public String periodoAcademico;

    @Key
    public Course[] inscripciones;

}
