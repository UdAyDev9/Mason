package narasimhaa.com.mitraservice.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {

    static final String PREF_USER_NAME= "username";
    static final String PREF_LOGIN_STATE= "Login_State";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setLoginStatus(Context ctx, boolean isLoggedIn)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_LOGIN_STATE, isLoggedIn);
        editor.commit();
    }

    public static void setValue(Context ctx, String key,String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(key,value);
        editor.commit();
    }


    public static boolean getLoginState(Context ctx){

        return getSharedPreferences(ctx).getBoolean(PREF_LOGIN_STATE,false);
    }

    public static String getValue(Context ctx,String key){

        return getSharedPreferences(ctx).getString(key,"N/A");
    }

}
