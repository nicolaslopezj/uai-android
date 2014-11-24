package cl.uai.uai.courses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.CoursesDetailRequest;
import cl.uai.uai.api.SportActionRequest;
import cl.uai.uai.api.json.Course;
import cl.uai.uai.api.json.CourseDetail;
import cl.uai.uai.api.json.CourseDetailGrade;
import cl.uai.uai.api.json.CourseDetailTeacher;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.api.json.Success;
import cl.uai.uai.main.BaseActivity;
import cl.uai.uai.sports.SportsDetail;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class CoursesDetail extends BaseActivity {

    public Course _course;
    public CourseDetail course;
    protected TeachersArrayAdapter teachersAdapter;
    protected GradesArrayAdapter gradesAdapter;
    protected ListView teachersListView;
    protected ListView gradesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_detail);

        _course = (Course) getIntent().getSerializableExtra("Course");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(_course.getName());

        teachersAdapter = new TeachersArrayAdapter(getBaseContext());
        teachersListView = (ListView) findViewById(R.id.teachersListView);
        teachersListView.setAdapter(teachersAdapter);

        gradesAdapter = new GradesArrayAdapter(getBaseContext());
        gradesListView = (ListView) findViewById(R.id.gradesListView);
        gradesListView.setAdapter(gradesAdapter);

        performRequest();
    }

    protected void setupView() {
        TextView finalGradeTextView = (TextView) findViewById(R.id.finalGradeTextView);
        finalGradeTextView.setText(course.final_grade);
        if (!course.final_grade.equals("0")) {
            finalGradeTextView.setVisibility(View.VISIBLE);
        }
        finalGradeTextView.setBackgroundResource(course.isGradeBlue() ? R.drawable.sport_reserved_text : R.drawable.course_status_background);

        TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setText(course.final_status);
        statusTextView.setVisibility(View.VISIBLE);
        int background = R.drawable.course_status_default_background;
        if (course.final_status.matches("(?i)aprobado")) {
            background = R.drawable.sport_reserved_text;
        }
        if (course.final_status.matches("(?i)reprobado")) {
            background = R.drawable.course_status_background;
        }
        statusTextView.setBackgroundResource(background);

        TextView announcementLabelTextView = (TextView) findViewById(R.id.announcementLabelTextView);
        announcementLabelTextView.setText(course.announcement);

        if (course.announcement.equals("")) {
            announcementLabelTextView.setVisibility(View.GONE);
        }

        teachersAdapter.notifyDataSetChanged();
        gradesAdapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(teachersListView);
        setListViewHeightBasedOnChildren(gradesListView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void performRequest() {
        CoursesDetailRequest request = new CoursesDetailRequest(_course.id);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new CoursesDetailRequestListener());
    }

    private class CoursesDetailRequestListener implements RequestListener<CourseDetail> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            showError("Ocurrió un error al cargar la información del curso");
        }

        @Override
        public void onRequestSuccess(CourseDetail response) {
            //update your UI
            course = response;
            setupView();
        }
    }

    private class TeachersArrayAdapter extends ArrayAdapter<CourseDetailTeacher> {
        private final Context context;

        public TeachersArrayAdapter(Context context) {
            super(context, R.layout.courses_detail_row_teacher);
            this.context = context;
        }

        @Override
        public int getCount() {
            if (course != null) {
                return course.led_by.length;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final CourseDetailTeacher teacher = course.led_by[position];
            View rowView = inflater.inflate(R.layout.courses_detail_row_teacher, parent, false);

            TextView roleTextView = (TextView) rowView.findViewById(R.id.roleTextView);
            roleTextView.setText(teacher.getRole());

            TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            nameTextView.setText(teacher.getFullName());

            TextView emailTextView = (TextView) rowView.findViewById(R.id.emailTextView);
            emailTextView.setText(teacher.getEmail());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", teacher.getEmail(), null));
                    startActivity(Intent.createChooser(emailIntent, "Enviar un email a " + teacher.getFullName()));
                }
            });

            return rowView;
        }
    }

    private class GradesArrayAdapter extends ArrayAdapter<CourseDetailGrade> {
        private final Context context;

        public GradesArrayAdapter(Context context) {
            super(context, R.layout.courses_detail_row_grade);
            this.context = context;
        }

        @Override
        public int getCount() {
            if (course != null) {
                Log.i("Grade", "Count: " + course.grades.length);
                return course.grades.length;
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final CourseDetailGrade grade = course.grades[position];
            View rowView = inflater.inflate(R.layout.courses_detail_row_grade, parent, false);

            TextView dateTextView = (TextView) rowView.findViewById(R.id.dateTextView);
            dateTextView.setText(grade.getDateNoTime());

            TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            nameTextView.setText(grade.name);

            TextView gradeTextView = (TextView) rowView.findViewById(R.id.gradeTextView);
            gradeTextView.setText(grade.grade);
            gradeTextView.setBackgroundResource(grade.isBlue() ? R.drawable.sport_reserved_text : R.drawable.course_status_background);

            return rowView;
        }
    }

}
