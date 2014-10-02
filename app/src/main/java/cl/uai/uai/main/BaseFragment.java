package cl.uai.uai.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

import cl.uai.uai.menu.Main;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class BaseFragment extends Fragment {

    protected Activity activity;
    protected SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void showError(String description) {
        Context context = activity.getApplicationContext();
        CharSequence text = description;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onStart() {
        spiceManager.start(activity.getApplicationContext());
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        activity = _activity;
    }

}
