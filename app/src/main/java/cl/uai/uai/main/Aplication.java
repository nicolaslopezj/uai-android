package cl.uai.uai.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by nicolaslopezj on 03-09-14.
 */
public class Aplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
