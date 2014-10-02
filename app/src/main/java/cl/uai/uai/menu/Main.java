package cl.uai.uai.menu;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cl.uai.uai.buses.Buses;
import cl.uai.uai.configuration.Configuration;
import cl.uai.uai.courses.Courses;
import cl.uai.uai.events.Events;
import cl.uai.uai.home.Home;
import cl.uai.uai.R;
import cl.uai.uai.main.Helper;
import cl.uai.uai.messages.Messages;
import cl.uai.uai.sports.Sports;
import cl.uai.uai.welcome.WelcomeSlidePagerActivity;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class Main extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        String[] values = getResources().getStringArray(R.array.navigation_items);
        String _identifier = values[position];

        if (_identifier.equals("Inicio")) {
            identifier = _identifier;
            mTitle = "Inicio";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Home())
                    .commit();
        } else if (_identifier.equals("Horarios")) {
            identifier = _identifier;
            mTitle = "Horarios";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Events())
                    .commit();
        } else if (_identifier.equals("Mensajes")) {
            identifier = _identifier;
            mTitle = "Mensajes";
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Messages())
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
        } else if (_identifier.equals("CONFIGURACIÓN")) {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        } else if (_identifier.equals("CERRAR SESIÓN")) {
            Intent intent = new Intent(this, WelcomeSlidePagerActivity.class);
            startActivity(intent);
            Helper.setToken(null);
            finish();
        } else {

        }
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
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }



}