package cl.uai.uai.configuration;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;

import cl.uai.uai.R;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Configuration extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Configuración");

        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
    }


    public static class Preferences extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference_fragmented);
        }
    }
}