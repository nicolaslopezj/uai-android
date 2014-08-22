package cl.uai.uai.messages;

import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dd.processbutton.iml.ActionProcessButton;

import cl.uai.uai.R;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.Sport;
import cl.uai.uai.main.BaseActivity;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class MessagesDetail extends BaseActivity {

    public Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = (Message) getIntent().getSerializableExtra("Message");

        setContentView(R.layout.sports_detail);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(message.from);
    }

}
