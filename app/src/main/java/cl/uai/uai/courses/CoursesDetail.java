package cl.uai.uai.courses;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import cl.uai.uai.R;
import cl.uai.uai.api.json.Course;
import cl.uai.uai.main.BaseActivity;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class CoursesDetail extends BaseActivity {

    public Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_detail);

        course = (Course) getIntent().getSerializableExtra("Course");
        setTitle(course.name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        return true;

        //return super.onOptionsItemSelected(item);
    }

}
