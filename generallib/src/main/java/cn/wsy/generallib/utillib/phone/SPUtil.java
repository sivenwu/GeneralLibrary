package cn.wsy.generallib.utillib.phone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 管理工具
 * Created by wsy on 2016/7/15.
 */
public class SPUtil {
    private static String PREFERENCE_NAME = "cn.defalut.name";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void initPreference(Context context,String preferenceName) {
        PREFERENCE_NAME = preferenceName;
        preferences = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public static boolean remove(String key) {
        return editor.remove(key).commit();
    }
}
