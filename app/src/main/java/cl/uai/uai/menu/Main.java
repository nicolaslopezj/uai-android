package cl.uai.uai.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.octo.android.robospice.JacksonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.concurrent.atomic.AtomicInteger;

import cl.uai.uai.api.PushSubscriptionRequest;
import cl.uai.uai.api.SportActionRequest;
import cl.uai.uai.api.json.Success;
import cl.uai.uai.buses.Buses;
import cl.uai.uai.calendar.CalendarMain;
import cl.uai.uai.configuration.Configuration;
import cl.uai.uai.courses.Courses;
import cl.uai.uai.faq.Faq;
import cl.uai.uai.home.Home;
import cl.uai.uai.R;
import cl.uai.uai.main.Helper;
import cl.uai.uai.messages.Messages;
import cl.uai.uai.sports.Sports;
import cl.uai.uai.welcome.WelcomeSlidePagerActivity;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class Main extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    protected SpiceManager spiceManager = new SpiceManager(JacksonGoogleHttpClientSpiceService.class);

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public String identifier;
    public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("APP", "initiating app");

        setContentView(R.layout.main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
        }

        onNavigationDrawerItemSelected(1);
        sendRegId();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        String[] values = getResources().getStringArray(R.array.navigation_items);
        String _identifier = values[position];

        Log.i("Navigation", "Navigating to: " + _identifier);

        if (_identifier.equals("Inicio")) {
            identifier = _identifier;
            mTitle = "Inicio";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Home())
                    .commit();
        } else if (_identifier.equals("Mensajes")) {
            identifier = _identifier;
            mTitle = "Mensajes";
                    fragmentManager
            .beginTransaction()
                    .replace(R.id.container, new Messages(99999))
                    .commit();
        } else if (_identifier.equals("Calendario")) {
            identifier = _identifier;
            mTitle = "Calendario";
                    fragmentManager
            .beginTransaction()
                    .replace(R.id.container, new CalendarMain())
                    .commit();
        } else if (_identifier.equals("Cursos")) {
            identifier = _identifier;
            mTitle = "Cursos";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Courses())
                    .commit();
        } else if (_identifier.equals("Deportes")) {
            identifier = _identifier;
            mTitle = "Deportes";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Sports())
                    .commit();
        } else if (_identifier.equals("Buses")) {
            identifier = _identifier;
            mTitle = "Buses";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Buses())
                    .commit();
        } else if (_identifier.equals("FAQ Y REGLAMENTOS")) {
            identifier = _identifier;
            mTitle = "FAQ y Reglamentos";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Faq())
                    .commit();
        } else if (_identifier.equals("CONFIGURACIÓN")) {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        } else if (_identifier.equals("CERRAR SESIÓN")) {
            Intent intent = new Intent(this, WelcomeSlidePagerActivity.class);
            startActivity(intent);
            Helper.setToken(null);
            Helper.setRegistrationIdSended(false);
            finish();
        } else {

        }
        restoreActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTitle);
        }
    }

    public void sendRegId() {
        if (!Helper.isRegistrationIdSended()) {
            String regid = Helper.getRegistrationId();
            if (regid.equals("")) {
                Log.i("GCM", "Regid is empty");
            } else {
                Log.i("GCM", "Sending reg id to server: " + regid);

                PushSubscriptionRequest request = new PushSubscriptionRequest(regid);
                String lastRequestCacheKey = request.createCacheKey();

                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new PushSubscriptionRequestListener());
            }
        }
    }

    private class PushSubscriptionRequestListener implements RequestListener<Success> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            showError("Ocurrio un error al registrar el dispositivo para notificaciones");
            Helper.setRegistrationIdSended(false);
        }

        @Override
        public void onRequestSuccess(Success response) {
            //update your UI
            Log.v("Request Success", "Regid action success:" + response.success);
            if (response.success) {
                //showError("Device registered");
                Helper.setRegistrationIdSended(true);
            } else {
                showError("Ocurrio un error al registrar el dispositivo para notificaciones");
                Helper.setRegistrationIdSended(false);
            }

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