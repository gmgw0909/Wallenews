package com.pipnet.wallenews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.pipnet.wallenews.App;

/**
 * Created by LeeBoo on 2017/09/14 0027.
 */

public class SPUtils {

    // SharedPreferences
    private static SharedPreferences getSharedPrefs() {
        return App.getInstance().getSharedPreferences("WL_SP", Context.MODE_PRIVATE);
    }

    public static String getString(String key, final String defaultValue) {
        return getSharedPrefs().getString(key, defaultValue);
    }

    public static void setString(final String key, final String value) {
        getSharedPrefs().edit().putString(key, value).commit();
    }

    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return getSharedPrefs().getBoolean(key, defaultValue);
    }

    public static void setBoolean(final String key, final boolean value) {
        getSharedPrefs().edit().putBoolean(key, value).commit();
    }

    public static void setInt(final String key, final int value) {
        getSharedPrefs().edit().putInt(key, value).commit();
    }

    public static int getInt(final String key, final int defaultValue) {
        return getSharedPrefs().getInt(key, defaultValue);
    }

    public static void setFloat(final String key, final float value) {
        getSharedPrefs().edit().putFloat(key, value).commit();
    }

    public static float getFloat(final String key, final float defaultValue) {
        return getSharedPrefs().getFloat(key, defaultValue);
    }

    public static void setLong(final String key, final long value) {
        getSharedPrefs().edit().putLong(key, value).commit();
    }

    public static long getLong(final String key, final long defaultValue) {
        return getSharedPrefs().getLong(key, defaultValue);
    }

    public static <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setObject(Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        setString(key, json);
    }

    public static void removeObject(Class<?> clazz) {
        remove(getKey(clazz));
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        getSharedPrefs().edit().remove(key);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        getSharedPrefs().edit().clear();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return getSharedPrefs().contains(key);
    }

}
