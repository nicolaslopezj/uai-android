package cl.uai.uai.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cl.uai.uai.menu.Main;
import cl.uai.uai.welcome.WelcomeSlidePagerActivity;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class App extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageLoader();

        Intent intent;
        if (Helper.isLoggedIn()) {
            intent = new Intent(this, Main.class);
        } else {
            intent = new Intent(this, WelcomeSlidePagerActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
    }

}
