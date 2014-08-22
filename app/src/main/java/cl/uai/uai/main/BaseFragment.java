package cl.uai.uai.main;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by nicolaslopezj on 18-08-14.
 */
public class BaseFragment extends Fragment {

    protected Activity activity;
    protected SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    protected void showError(String description) {

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
