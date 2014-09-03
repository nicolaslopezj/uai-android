package cl.uai.uai.messages;

import android.os.Bundle;
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
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class MessagesDetail extends BaseActivity {

    public Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_detail);

        message = (Message) getIntent().getSerializableExtra("Message");

        TextView fromTextView = (TextView) findViewById(R.id.fromTextView);
        fromTextView.setText(message.from);

        TextView contentTextView = (TextView) findViewById(R.id.contentTextView);
        contentTextView.setText(message.body);
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
