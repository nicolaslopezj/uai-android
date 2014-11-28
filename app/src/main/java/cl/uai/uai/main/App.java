package cl.uai.uai.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import cl.uai.uai.menu.Main;
import cl.uai.uai.welcome.WelcomeSlidePagerActivity;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class App extends Activity {

    /**
     * Code to make the notifications
     */
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCM";
    String SENDER_ID = "152811688136";
    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        initImageLoader();

        Intent intent;
        if (Helper.isLoggedIn()) {
            intent = new Intent(this, Main.class);
        } else {
            intent = new Intent(this, WelcomeSlidePagerActivity.class);
        }

        startActivity(intent);
        finish();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = Helper.getRegistrationId();

            if (regid.equals("")) {
                Log.i(TAG, "Registering to GCM");
                registerInBackground();
            }

            startActivity(intent);
            finish();
        } else {
            Log.e(TAG, "No valid Play Services APK Found");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("Check Play Services", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    Helper.saveRegistrationId(regid);
                } catch (IOException ex) {
                }
                return null;
            }
        }.execute(null, null, null);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
    }

}
