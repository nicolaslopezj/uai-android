package cl.uai.uai.calendar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import cl.uai.uai.R;
import cl.uai.uai.api.CalendarRequest;
import cl.uai.uai.api.EventsIndexRequest;
import cl.uai.uai.api.json.CalendarEvent;
import cl.uai.uai.api.json.Event;
import cl.uai.uai.main.BaseFragment;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 19-11-14.
 */
public class CalendarMain extends BaseFragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

    private WeekView mWeekView;
    private PullToRefreshLayout mPullToRefreshLayout;
    public CalendarEvent[] personalEvents;
    public CalendarEvent[] publicEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.calendar_main, null);

        personalEvents = new CalendarEvent[0];
        publicEvents = new CalendarEvent[0];

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

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) layout.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        return layout;
    }

    @Override
    public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
        showError(weekViewEvent.getName());
    }

    @Override
    public void onEventLongPress(WeekViewEvent weekViewEvent, RectF rectF) {
        showError(weekViewEvent.getName());
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Log.i("Calendar", "Rendering Events");

        int index = 1;
        for (CalendarEvent event : personalEvents) {
            Calendar startTime = stringToCalendar(event.Start);
            Calendar endTime =  stringToCalendar(event.End);

            if (newMonth == getMonthOfString(event.Start)) {
                WeekViewEvent _event = new WeekViewEvent(index, event.getBeatifulName(), startTime, endTime);
                _event.setColor(getResources().getColor(R.color.holo_blue_bright));
                events.add(_event);
            }
            index++;
        }

        for (CalendarEvent event : publicEvents) {
            Calendar startTime = stringToCalendar(event.Start);
            Calendar endTime =  stringToCalendar(event.End);

            if (newMonth == getMonthOfString(event.Start)) {
                WeekViewEvent _event = new WeekViewEvent(index, event.Summary, startTime, endTime);
                _event.setColor(getResources().getColor(R.color.holo_red_light));
                events.add(_event);
            }

            index++;
        }

        Log.i("Calendar", "Showing events n: " + events.toArray().length);

        return events;
    }

    private Calendar stringToCalendar(String date) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date d1 = null;
        Calendar tdy1;
        try {
            d1 = form.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        tdy1 = Calendar.getInstance();
        tdy1.setTime(d1);
        return tdy1;
    }

    private int getMonthOfString(String date) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        java.util.Date d1 = null;
        try {
            d1 = form.parse(date);
            return d1.getMonth();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        CalendarRequest request = new CalendarRequest(1);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_DAY, new CalendarRequestPersonalListener());

        CalendarRequest request2 = new CalendarRequest(2);
        String lastRequestCacheKey2 = request2.createCacheKey();

        spiceManager.execute(request2, lastRequestCacheKey2, DurationInMillis.ONE_DAY, new CalendarRequestPublicListener());
    }

    private class CalendarRequestPersonalListener implements RequestListener<CalendarEvent[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.e("Request Error", e.toString());
            showError("Ocurrió un error al descargar los datos");
            mPullToRefreshLayout.setRefreshComplete();
        }

        @Override
        public void onRequestSuccess(CalendarEvent[] response) {
            //update your UI
            Log.i("Request Success", "Downloaded " + response.length + " calendar events");
            personalEvents = response;
            mPullToRefreshLayout.setRefreshComplete();
            mWeekView.notifyDatasetChanged();
        }
    }

    private class CalendarRequestPublicListener implements RequestListener<CalendarEvent[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.e("Request Error", e.toString());
            showError("Ocurrió un error al descargar los datos");
        }

        @Override
        public void onRequestSuccess(CalendarEvent[] response) {
            //update your UI
            Log.i("Request Success", "Downloaded " + response.length + " calendar events");
            publicEvents = response;
            mWeekView.notifyDatasetChanged();
        }
    }
}