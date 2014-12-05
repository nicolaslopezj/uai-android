package cl.uai.uai.messages;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.HomeImagesRequest;
import cl.uai.uai.api.MessagesIndexRequest;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.main.BaseFragment;
import cl.uai.uai.main.Helper;
import cl.uai.uai.sports.Sports;
import cl.uai.uai.sports.SportsDetail;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Messages extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private SwipeRefreshLayout mPullToRefreshLayout;
    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;
    public Message[] messages;
    public int maxMessages;
    public boolean isLongClick;

    public Messages(int _maxMessages) {
        maxMessages = _maxMessages;
    }

    @Override
    public void onResume() {
        messages = Helper.getNotDeletedMessagesList();
        adapter.notifyDataSetChanged();

        super.onResume();

        if (maxMessages > 10 && !Helper.isMessageTutorialViewed()) {
            Helper.setMessageTutorialViewed(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ShowcaseView sv = new ShowcaseView.Builder(getActivity(), false)
                            //.setTarget(viewTarget)
                            .setContentTitle("Instrucciones")
                            .hideOnTouchOutside()
                            .setContentText("Toca un mensaje para ver el detalle.\nDeja presionado el mensaje para elimarlo.")
                            .setStyle(R.style.CustomShowcaseTheme)
                            .build();

                    RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                    lps.setMargins(margin, margin, margin, 100);
                    sv.setButtonPosition(lps);
                }
            }, 150);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messages = Helper.getNotDeletedMessagesList();

        final View layout = inflater.inflate(R.layout.messages_main, null);

        itemsListView = (ListView) layout.findViewById(R.id.itemsListView);

        adapter = new ItemsArrayAdapter(layout.getContext());
        itemsListView.setAdapter(adapter);
        itemsListView.setOnItemLongClickListener(this);
        itemsListView.setOnItemClickListener(this);

        mPullToRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.ptr_layout);
        mPullToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                performRequest();
            }
        });
        if (savedInstanceState == null) {
            mPullToRefreshLayout.setRefreshing(true);
            performRequest();
        }

        return layout;
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        MessagesIndexRequest request = new MessagesIndexRequest();
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new MessagesIndexRequestListener());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isLongClick) {
            return;
        }
        final Message message = messages[position];
        Intent intent = new Intent(activity, MessagesDetail.class);
        intent.putExtra("Message", message);
        activity.startActivity(intent);
        Helper.readMessage(message);
        messages = Helper.getNotDeletedMessagesList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isLongClick = true;
        final Message message = messages[position];
        AlertDialog alert = new AlertDialog.Builder(activity)
                .setTitle("Borrar Mensaje")
                .setMessage("Estas seguro que quieres borrar este mensaje?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.deleteMessage(message);
                        messages = Helper.getNotDeletedMessagesList();
                        adapter.notifyDataSetChanged();
                        isLongClick = false;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isLongClick = false;
                    }
                })
                .create();
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isLongClick = false;
            }
        });
        alert.show();
        return true;
    }

    private class MessagesIndexRequestListener implements RequestListener<Message[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            mPullToRefreshLayout.setRefreshing(false);
            showError("Ocurrió un error al descargar los datos");
        }

        @Override
        public void onRequestSuccess(Message[] response) {
            //update your UI
            Log.v("Request Success", "Downloaded " + response.length + " messages");
            mPullToRefreshLayout.setRefreshing(false);
            messages = Helper.getNotDeletedMessagesList();
            adapter.notifyDataSetChanged();
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
            return messages.length > maxMessages ? maxMessages : messages.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final Message message = messages[position];
            View rowView = inflater.inflate(R.layout.messages_row, parent, false);

            MaterialRippleLayout.on(rowView)
                    .rippleColor(R.color.grey_400)
                    .create();

            TextView fromTextView = (TextView) rowView.findViewById(R.id.fromTextView);
            fromTextView.setText(message.getFrom());

            TextView dateTextView = (TextView) rowView.findViewById(R.id.dateTextView);
            dateTextView.setText(message.getDateParsed());

            TextView previewTextView = (TextView) rowView.findViewById(R.id.previewTextView);
            previewTextView.setText(message.title);

            View unreadCircleView = rowView.findViewById(R.id.unreadCircleView);
            unreadCircleView.setVisibility(message.readed ? View.GONE : View.VISIBLE);

            return rowView;
        }
    }

}
