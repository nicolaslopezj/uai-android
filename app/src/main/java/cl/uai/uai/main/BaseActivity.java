package cl.uai.uai.main;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by nicolaslopezj on 22-08-14.
 */
public class BaseActivity extends Activity{

    protected SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    protected void showError(String description) {
        Context context = getApplicationContext();
        CharSequence text = description;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onStart() {
        spiceManager.start(getApplicationContext());
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

}
