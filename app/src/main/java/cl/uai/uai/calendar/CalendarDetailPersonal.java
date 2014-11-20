package cl.uai.uai.calendar;

import android.os.Bundle;
import android.widget.TextView;

import cl.uai.uai.R;
import cl.uai.uai.api.json.CalendarEvent;
import cl.uai.uai.main.BaseActivity;

/**
 * Created by nicolaslopezj on 20-11-14.
 */
public class CalendarDetailPersonal extends BaseActivity {

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

        setContentView(R.layout.calendar_detail_personal);

        TextView organizerTextView = (TextView) findViewById(R.id.organizerTextView);
        organizerTextView.setText(event.getTeacherName());

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(event.getNameWithSection());

        TextView roomTextView = (TextView) findViewById(R.id.roomTextView);
        roomTextView.setText(event.getRoom());

        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(event.getStartAndEndDate());
    }


}
