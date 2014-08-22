package cl.uai.uai.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import cl.uai.uai.api.EventsIndexRequest;
import cl.uai.uai.api.json.Event;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.main.BaseFragment;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 19-08-14.
 */
public class Events extends BaseFragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;
    public Event[] events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        events = new Event[0];

        View layout = inflater.inflate(R.layout.events_main, null);

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

        //

        if (savedInstanceState == null) {
            mPullToRefreshLayout.setRefreshing(true);
            performRequest();
        }

        return layout;
    }

    public void setEvents(Event[] _events) {
        events = _events;
        adapter.notifyDataSetChanged();
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String location = prefs.getString("events_location", "pregrado");
        EventsIndexRequest request = new EventsIndexRequest(location);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new EventsIndexRequestListener());
    }

    private class EventsIndexRequestListener implements RequestListener<Event[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            showError("Ocurrió un error al descargar los datos");
            mPullToRefreshLayout.setRefreshComplete();
        }

        @Override
        public void onRequestSuccess(Event[] response) {
            //update your UI
            Log.v("Request Success", "Downloaded " + response.length + " events");
            mPullToRefreshLayout.setRefreshComplete();
            setEvents(response);
        }
    }

    private class ItemsArrayAdapter extends ArrayAdapter<Message> {
        private final Context context;

        public ItemsArrayAdapter(Context context) {
            super(context, R.layout.messages_row);
            this.context = context;
        }

        @Override
        public int getCount() {
            return events.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final Event event = events[position];
            View rowView = inflater.inflate(R.layout.events_row, parent, false);

            TextView typeTextView = (TextView) rowView.findViewById(R.id.typeTextView);
            if (event.type.equals("")) {
                typeTextView.setVisibility(View.GONE);
            } else {
                typeTextView.setVisibility(View.VISIBLE);
                typeTextView.setText(event.type);
            }

            TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            nameTextView.setText(event.name);

            TextView extrasTextView = (TextView) rowView.findViewById(R.id.extrasTextView);
            String extras = "";
            if (event.section > 0 && !event.teacher.equals("")) {
                extras = "Sec. " + event.section + " - " + event.teacher;
                extrasTextView.setVisibility(View.VISIBLE);
            } else if (event.section > 0){
                extras = "Sec. " + event.section;
                extrasTextView.setVisibility(View.VISIBLE);
            } else if (!event.teacher.equals("")) {
                extras = event.teacher;
                extrasTextView.setVisibility(View.VISIBLE);
            } else {
                extrasTextView.setVisibility(View.GONE);
            }
            extrasTextView.setText(extras);

            TextView dateTextView = (TextView) rowView.findViewById(R.id.dateTextView);
            dateTextView.setText(event.date);

            TextView roomTextView = (TextView) rowView.findViewById(R.id.roomTextView);
            if (event.suspended) {
                roomTextView.setText("Suspendida");
            } else {
                roomTextView.setText(event.room);
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //setContent(identifier);
                }
            });

            return rowView;
        }
    }

}
