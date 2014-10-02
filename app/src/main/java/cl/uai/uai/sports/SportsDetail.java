package cl.uai.uai.sports;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dd.processbutton.iml.ActionProcessButton;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.SportActionRequest;
import cl.uai.uai.api.SportsIndexRequest;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.api.json.Success;
import cl.uai.uai.main.BaseActivity;
import cl.uai.uai.main.BaseFragment;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class SportsDetail extends BaseActivity {

    public Sport sport;
    private ActionProcessButton actionButton;
    private NumberProgressBar progressBar;
    private TextView availableTextView;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slidein_up, R.anim.slideout_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sport = (Sport) getIntent().getSerializableExtra("Sport");

        setContentView(R.layout.sports_detail);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(sport.name);

        TextView teacherTextView = (TextView) findViewById(R.id.teacherTextView);
        teacherTextView.setText(sport.led_by);

        TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
        locationTextView.setText(sport.location);

        availableTextView = (TextView) findViewById(R.id.availableTextView);
        availableTextView.setText(sport.available + " de " + sport.capacity + " cupos disponibles");

        progressBar = (NumberProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(sport.capacity);
        progressBar.setProgress(sport.capacity - sport.available);

        actionButton = (ActionProcessButton) findViewById(R.id.actionButton);
        actionButton.setMode(ActionProcessButton.Mode.ENDLESS);
        actionButton.setText(sport.reserved ? "Cancelar" : "Reservar");
        actionButton.setCompleteText(sport.reserved ? "Cancelado" : "Reservado");

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionProcessButton button = (ActionProcessButton) view;
                if (button.getProgress() == 0) {
                    button.setProgress(1);
                    performRequest();
                }
            }
        });

        Log.v("Want to reserve/cancel", sport.name);
    }


    private void performRequest() {
        String action = sport.reserved ? "cancellation" : "reservation";

        SportActionRequest request = new SportActionRequest(action, sport.id);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new SportActionRequestListener());
    }

    private class SportActionRequestListener implements RequestListener<Success> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            actionButton.setProgress(-1);
            showError("Ocurrió un error al reservar");
        }

        @Override
        public void onRequestSuccess(Success response) {
            //update your UI
            Log.v("Request Success", "Sport action success:" + response.success);
            if (response.success) {
                actionButton.setProgress(100);
                Integer available = sport.reserved ? sport.available +1 : sport.available -1;
                availableTextView.setText(available + " de " + sport.capacity + " cupos disponibles");
                progressBar.setProgress(sport.capacity - available);
            } else {
                showError("Ocurrió un error al reservar");
                actionButton.setProgress(-1);
            }

        }
    }

}
