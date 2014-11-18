package cl.uai.uai.courses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.CoursesIndexRequest;
import cl.uai.uai.api.MessagesIndexRequest;
import cl.uai.uai.api.json.Course;
import cl.uai.uai.api.json.CoursesPeriod;
import cl.uai.uai.main.BaseFragment;
import cl.uai.uai.messages.MessagesDetail;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class Courses  extends BaseFragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;
    public CoursesPeriod[] periods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        periods = new CoursesPeriod[0];

        View layout = inflater.inflate(R.layout.courses_main, null);

        itemsListView = (ListView) layout.findViewById(R.id.itemsListView);

        adapter = new ItemsArrayAdapter(layout.getContext());
        itemsListView.setAdapter(adapter);

        mPullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(activity)
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        performRequest();
                    }
                })
                .setup(mPullToRefreshLayout);

        if (savedInstanceState == null) {
            mPullToRefreshLayout.setRefreshing(true);
            performRequest();
        }

        return layout;
    }

    public void setPeriods(CoursesPeriod[] _periods) {
        periods = _periods;
        adapter.notifyDataSetChanged();
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        CoursesIndexRequest request = new CoursesIndexRequest();
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new MessagesIndexRequestListener());
    }

    private class MessagesIndexRequestListener implements RequestListener<CoursesPeriod[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            mPullToRefreshLayout.setRefreshing(false);
            showError("Ocurrió un error al descargar los datos");
        }

        @Override
        public void onRequestSuccess(CoursesPeriod[] response) {
            //update your UI
            Log.v("Request Success", "Downloaded " + response.length + " periods");
            mPullToRefreshLayout.setRefreshing(false);
            setPeriods(response);
        }
    }

    private class ItemsArrayAdapter extends ArrayAdapter<CoursesPeriod> {
        private final Context context;

        public ItemsArrayAdapter(Context context) {
            super(context, R.layout.courses_row);
            this.context = context;
        }

        @Override
        public int getCount() {
            int count  = 0;
            for (CoursesPeriod period : periods) {
                count += period.inscripciones.length;
                count ++;
            }
            return count;
        }

        public boolean isPeriod(int index) {
            int k = 0;
            for (CoursesPeriod period : periods) {
                if (index == k) {
                    return true;
                }
                k++;
                for (Course course : period.inscripciones) {
                    if (index == k) {
                        return false;
                    }
                    k++;
                }
            }
            return false;
        }

        public CoursesPeriod getPeriodAtIndex(int index) {
            int k = 0;
            for (CoursesPeriod period : periods) {
                if (index == k) {
                    return period;
                }
                k++;

                for (Course course : period.inscripciones) {
                    if (index == k) {
                        return null;
                    }
                    k++;
                }
            }
            return null;
        }

        public Course getCourseAtIndex(int index) {
            int k = 0;
            for (CoursesPeriod period : periods) {
                if (index == k) {
                    return null;
                }
                k++;
                for (Course course : period.inscripciones) {
                    if (index == k) {
                        return course;
                    }
                    k++;
                }
            }
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (isPeriod(position)) {
                View rowView = inflater.inflate(R.layout.courses_row_header, parent, false);
                final CoursesPeriod period = getPeriodAtIndex(position);
                TextView titleTextView = (TextView) rowView.findViewById(R.id.titleTextView);
                titleTextView.setText(period.periodoAcademico);
                return rowView;
            } else {
                View rowView = inflater.inflate(R.layout.courses_row, parent, false);
                final Course course = getCourseAtIndex(position);
                TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
                nameTextView.setText(course.getRealName());

                rowView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //setContent(identifier);
                        Intent intent = new Intent(activity, CoursesDetail.class);
                        intent.putExtra("Course", course);
                        activity.startActivity(intent);
                        //activity.overridePendingTransition(R.anim.slidein_right, R.anim.fadeout);
                    }
                });

                return rowView;
            }
        }
    }

}
