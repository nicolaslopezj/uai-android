package cl.uai.uai.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class Helper {

    public static void setToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String getToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        return preferences.getString("token", null);
    }

    public static boolean isLoggedIn() {
        if (getToken() != null) {
            return true;
        }

        return false;
    }

}
