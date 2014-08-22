package cl.uai.uai.sports;

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
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.SportsIndexRequest;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.main.BaseFragment;
import cl.uai.uai.welcome.WelcomeSlidePagerActivity;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Sports extends BaseFragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;
    public Sport[] sports;
    public Boolean hasReserved;

    @Override
    public void onResume() {
        super.onResume();
        mPullToRefreshLayout.setRefreshing(true);
        performRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sports = new Sport[0];

        View layout = inflater.inflate(R.layout.sports_main, null);

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

        return layout;
    }

    public void setSports(Sport[] _sports) {
        sports = _sports;
        adapter.notifyDataSetChanged();
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        SportsIndexRequest request = new SportsIndexRequest();
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new SportsIndexRequestListener());
    }

    private class SportsIndexRequestListener implements RequestListener<Sport[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            mPullToRefreshLayout.setRefreshing(false);
            showError("Ocurrió un error al descargar los datos");
        }

        @Override
        public void onRequestSuccess(Sport[] response) {
            //update your UI
            Log.v("Request Success", "Downloaded " + response.length + " sports");
            mPullToRefreshLayout.setRefreshing(false);
            setSports(response);
        }
    }

    private class ItemsArrayAdapter extends ArrayAdapter<Sport> {
        private final Context context;

        public ItemsArrayAdapter(Context context) {
            super(context, R.layout.sports_row);
            this.context = context;
        }

        @Override
        public int getCount() {
            return sports.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final Sport sport = sports[position];
            View rowView = inflater.inflate(R.layout.sports_row, parent, false);

            TextView fromTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            fromTextView.setText(sport.name);

            TextView teacherTextView = (TextView) rowView.findViewById(R.id.teacherTextView);
            teacherTextView.setText(sport.led_by);

            TextView reservedTextView = (TextView) rowView.findViewById(R.id.reservedTextView);
            reservedTextView.setText(sport.reserverd ? "RESERVADO" : sport.available + "/" + sport.capacity);

            if (sport.reserverd) {
                hasReserved = true;
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //setContent(identifier);
                    if (sport.available < 1) {
                        return;
                    }

                    if  (hasReserved && !sport.reserverd) {
                        showError("Ya tienes una reserva");
                        return;
                    }

                    Intent intent = new Intent(activity, SportsDetail.class);
                    intent.putExtra("Sport", sport);
                    activity.startActivity(intent);
                }
            });

            return rowView;
        }
    }

}
