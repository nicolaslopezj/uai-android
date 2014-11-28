package cl.uai.uai.messages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dd.processbutton.iml.ActionProcessButton;

import cl.uai.uai.R;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.main.BaseActivity;
import cl.uai.uai.main.BaseFragment;
import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class MessagesDetail extends BaseActivity {

    public Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        message = (Message) getIntent().getSerializableExtra("Message");

        TextView fromTextView = (TextView) findViewById(R.id.fromTextView);
        fromTextView.setText(message.getFrom());

        TextView contentTextView = (TextView) findViewById(R.id.contentTextView);
        contentTextView.setText(message.body);

        message.markAsRead();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        return true;

        //return super.onOptionsItemSelected(item);
    }

}
