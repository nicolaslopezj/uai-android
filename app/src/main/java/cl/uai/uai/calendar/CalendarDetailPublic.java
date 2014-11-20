package cl.uai.uai.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dd.processbutton.iml.ActionProcessButton;

import cl.uai.uai.R;
import cl.uai.uai.api.json.CalendarEvent;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.main.BaseActivity;

/**
 * Created by nicolaslopezj on 20-11-14.
 */
public class CalendarDetailPublic extends BaseActivity {

    public CalendarEvent event;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slidein_up, R.anim.slideout_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = (CalendarEvent) getIntent().getSerializableExtra("Event");

        setContentView(R.layout.calendar_detail_public);

        TextView organizerTextView = (TextView) findViewById(R.id.organizerTextView);
        organizerTextView.setText(event.Organizer);

        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(event.Description);

        TextView summaryTextView = (TextView) findViewById(R.id.summaryTextView);
        summaryTextView.setText(event.Summary);

        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(event.getStartAndEndDate());
    }


}
