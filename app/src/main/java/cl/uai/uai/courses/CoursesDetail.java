package cl.uai.uai.courses;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
        setContentView(R.layout.courses_detail);

        course = (Course) getIntent().getSerializableExtra("Course");
        setTitle(course.getRealName());

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(course.getRealName());

        TextView sectionTextView = (TextView) findViewById(R.id.sectionTextView);
        if (course.getSection() != null) {
            sectionTextView.setVisibility(View.VISIBLE);
            sectionTextView.setText("Sección " + course.getSection());
        } else {
            sectionTextView.setVisibility(View.GONE);
        }
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
