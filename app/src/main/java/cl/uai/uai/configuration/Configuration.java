package cl.uai.uai.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import cl.uai.uai.R;
import cl.uai.uai.api.MessageChannelSubscriptionRequest;
import cl.uai.uai.api.MessagesIndexRequest;
import cl.uai.uai.api.json.Message;
import cl.uai.uai.api.json.Success;
import cl.uai.uai.main.Aplication;
import cl.uai.uai.main.BaseActivity;
import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Configuration extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Configuración");

        getFragmentManager().beginTransaction().replace(R.id.content_wrapper, new Preferences()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(Aplication.getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(Aplication.getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("messages_pregrado") ||
            s.equals("messages_eventos_uai") ||
            s.equals("messages_asuntos_estudiantiles") ||
            s.equals("messages_deportes") ||
            s.equals("messages_finanzas")) {
            String channelName = s.replace("messages_", "");
            performRequest(channelName, Helper.isSubscribedTo(channelName));
        }
        Log.i("Preference Change", s);
    }

    private void performRequest(String channelName, boolean enabled) {
        MessageChannelSubscriptionRequest request = new MessageChannelSubscriptionRequest(channelName, enabled);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new MessageChannelSubscriptionRequestListener());
    }

    private class MessageChannelSubscriptionRequestListener implements RequestListener<Success> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.e("Request Error", e.toString());
            showError("Ocurrió un error guardar la preferencia en omega");
        }

        @Override
        public void onRequestSuccess(Success response) {
            //update your UI
            Log.i("Request Success", "Success " + response.success);
        }
    }


    public static class Preferences extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference_fragmented);
        }
    }

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