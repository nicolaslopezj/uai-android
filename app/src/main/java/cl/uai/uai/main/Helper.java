package cl.uai.uai.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cl.uai.uai.api.json.Message;

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

    public static boolean isLoggedIn() {
        if (getToken() != null) {
            return true;
        }
        return false;
    }

    public static void setPreferencesToDefaultValues() {
        setMessageTutorialViewed(false);
        setLastMessagesRequestDate("");
        setMessagesList(new Message[0]);
        String[] keys = {"messages_eventos_uai", "messages_asuntos_estudiantiles", "messages_deportes", "messages_finanzas"};
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Aplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putBoolean(keys[i], true);
        }
        editor.commit();
    }

    public static String getOpenMessageId() {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        return tinydb.getString("open_message_id");
    }

    public static void setOpenMessageId(String messageId) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        tinydb.putString("open_message_id", messageId);
    }

    public static void setMessageTutorialViewed(Boolean viewed) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        tinydb.putBoolean("message_tutorial_viewed", viewed);
    }

    public static Boolean isMessageTutorialViewed() {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        return tinydb.getBoolean("message_tutorial_viewed");
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

    public static void setLastMessagesRequestDate(String date) {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        tinydb.putString("last_messages_request_date", date);
    }

    public static String getLastMessageRequestDate() {
        TinyDB tinydb = new TinyDB(Aplication.getContext());
        return tinydb.getString("last_messages_request_date").equals("") ? "2012-12-02T13:27:29" : tinydb.getString("last_messages_request_date");
    }

    public static void addToMessagesList(Message message) {
        if (!isInMessagesList(message)) {
            Message[] messages = getMessagesList();
            messages = concatMessages(messages, new Message[]{message});
            setMessagesList(messages);
        }
    }

    public static Message[] concatMessages(Message[] A, Message[] B) {
        int aLen = A.length;
        int bLen = B.length;
        Message[] C= new Message[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    public static boolean isInMessagesList(Message message) {
        Message[] messages = getMessagesList();
        for (Message message1 : messages) {
            if (message1.id.equals(message.id)) {
                return true;
            }
        }
        return false;
    }

    public static void readMessage(Message message) {
        Message[] messages = getMessagesList();
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].id.equals(message.id)) {
                messages[i].readed = true;
            }
        }
        setMessagesList(messages);
    }

    public static void deleteMessage(Message message) {
        Message[] messages = getMessagesList();
        for (int i = 0; i < messages.length; i++) {
            if (messages[i].id.equals(message.id)) {
                messages[i].deleted = true;
            }
        }
        setMessagesList(messages);
    }

    public static void setMessagesList(Message[] messages) {
        try {
            DB snappydb = DBFactory.open(Aplication.getContext()); //create or open an existing databse using the default name
            snappydb.put("messages", messages);
        } catch (SnappydbException e) {
            Log.e("DB ERROR", e.getStackTrace().toString());
        }
    }

    public static Message[] getNotDeletedMessagesList() {
        try {
            DB snappydb = DBFactory.open(Aplication.getContext()); //create or open an existing databse using the default name
            Message[] messages = snappydb.getArray("messages", Message.class);// get array of string
            snappydb.close();
            Message[] finalMessages = messages;

            for (int i = 0; i < messages.length; i++) {
                if (messages[i].deleted) {
                    finalMessages = ArrayUtils.removeElement(finalMessages, messages[i]);
                }
            }

            return finalMessages;
        } catch (SnappydbException e) {
            Log.e("DB ERROR", e.getStackTrace().toString());
        }

        return new Message[0];
    }

    public static Message[] getMessagesList() {
        try {
            DB snappydb = DBFactory.open(Aplication.getContext()); //create or open an existing databse using the default name
            Message[] messages  =  snappydb.getArray("messages", Message.class);// get array of string
            snappydb.close();
            return messages;
        } catch (SnappydbException e) {
            Log.e("DB ERROR", e.getStackTrace().toString());
        }

        return new Message[0];
    }

}
