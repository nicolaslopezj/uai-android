package cl.uai.uai.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class Helper {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

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

    public static void setUsername(String username) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static String getUsername() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        return preferences.getString("username", null);
    }

    public static boolean isSubscribedTo(String sender) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        return preferences.getBoolean("messages_" + sender, true);
    }

    public static void setMessageReaded(String id) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        tinydb.putBoolean("message_readed_" + id, true);
    }

    public static boolean isMessageReaded(String id) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        return tinydb.getBoolean("message_readed_" + id);
    }

    public static boolean isLoggedIn() {
        if (getToken() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion() {
        try {
            PackageInfo packageInfo = Aplication.getContext().getPackageManager()
                    .getPackageInfo(Aplication.getContext().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void saveRegistrationId(String regid) {
        int appVersion = getAppVersion();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        Log.i("GCM", "Saving regId on app version " + appVersion);
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static void setRegistrationIdSended(boolean sended) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        tinydb.putBoolean("registration_id_sended", sended);
    }

    public static boolean isRegistrationIdSended() {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        return tinydb.getBoolean("registration_id_sended");
    }

    public static String getRegistrationId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.equals("")) {
            Log.i("GCM", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i("GCM", "App version changed.");
            return "";
        }
        return registrationId;
    }

}
