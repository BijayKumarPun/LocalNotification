package io.test.ntf;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class represents ..
 * <p>
 * Created on 7/18/19 at 10:50 AM
 *
 * @author bj
 */
public class TimeData {
    public static final int start_time = 7;
    public static final int end_time = 11;
    private int hr_default = 13; //10 oclock
    private int min_default = 11;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String key_shared_pref = "shared_pref";
    private String key_hr = "hour";
    private String key_min = "min";
    private String key_stop_forever = "stop_forever";

    public TimeData(Context context) {
        sharedPreferences = context.getSharedPreferences(key_shared_pref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void set(int hr, int min) {
        editor.putInt(key_hr, hr);
        editor.putInt(key_min, min);
        editor.apply();
    }

    public int getHr() {
        return sharedPreferences.getInt(key_hr, hr_default);

    }

    public int getMin() {
        return sharedPreferences.getInt(key_min, min_default);
    }


    public void setStopForever(boolean stopForever) {
        editor.putBoolean(key_stop_forever, stopForever);
        editor.apply();

    }

    public boolean isStoppedForever() {
        return sharedPreferences.getBoolean(key_stop_forever, false);

    }
}
