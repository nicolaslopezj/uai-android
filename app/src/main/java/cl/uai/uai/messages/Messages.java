package cl.uai.uai.messages;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.HomeImagesRequest;
import cl.uai.uai.api.MessagesIndexRequest;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.main.BaseFragment;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Messages extends BaseFragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;
    public Message[] messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messages = new Message[0];

        View layout = inflater.inflate(R.layout.messages_main, null);

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

    public void setMessages(Message[] _messages) {
        messages = _messages;
        adapter.notifyDataSetChanged();
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        MessagesIndexRequest request = new MessagesIndexRequest();
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new MessagesIndexRequestListener());
    }

    private class MessagesIndexRequestListener implements RequestListener<Message[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            mPullToRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onRequestSuccess(Message[] response) {
            //update your UI
            Log.v("Request Success", "Downloaded " + response.length + " messages");
            mPullToRefreshLayout.setRefreshing(false);
            setMessages(response);
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
            return messages.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final Message message = messages[position];
            View rowView = inflater.inflate(R.layout.messages_row, parent, false);

            TextView fromTextView = (TextView) rowView.findViewById(R.id.fromTextView);
            fromTextView.setText(message.from);

            TextView previewTextView = (TextView) rowView.findViewById(R.id.previewTextView);
            previewTextView.setText(message.body);

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
