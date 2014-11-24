package cl.uai.uai.main;

import android.util.Log;

/**
 * Created by nicolaslopezj on 24-11-14.
 */
public class StringsHelper {

    public static String lowercase(String initial) {
        return initial.toLowerCase();
    }

    public static String uppercase(String initial) {
        return initial.toUpperCase();
    }

    public static String makeItBeautiful(String initial) {
        if (initial.equals("")) {
            return initial;
        }

        StringBuffer res = new StringBuffer();

        String[] strArr = lowercase(initial).split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            if (stringArray.length > 2) {
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                str = new String(stringArray);
                res.append(str).append(" ");
            } else if (stringArray.length > 0) {
                res.append(uppercase(str)).append(" ");
            }
        }

        String almostReadyString = res.toString().trim();

        return upperUppers(lowerLowers(almostReadyString)).trim();
    }

    public static String lowerLowers(String initial) {
        String[] lowers = {"Y", "DE", "A", "LA", "E"};
        for (String lower : lowers) {
            String search = " " + lower + " ";
            String replace = " " + lowercase(lower) + " ";
            initial = initial.replace(search, replace);
        }

        return initial;
    }

    public static String upperUppers(String initial) {
        String[] uppers = {"Iii", "Vii", "Viii", "Uai"};
        for (String upper : uppers) {
            String search = " " + upper + " ";
            String replace = " " + uppercase(upper) + " ";
            initial = initial.replace(search, replace);

            if (initial.endsWith(" " + upper)) {
                search = " " + upper;
                replace = " " + uppercase(upper);
                initial = initial.replace(search, replace);
            }
        }

        return initial;
    }

}
