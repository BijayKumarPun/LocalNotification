package io.test.ntf;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * This class represents ..
 * <p>
 * Created on 7/18/19 at 10:25 AM
 *
 * @author bj
 */
public class ScheduledNotifier {
    private String TAG = "scheduledNotification";
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;

    public static void setReminder(Context context, Class<?> cls, int hour, int min) {
        Log.i("scheduledNotification", "setReminder: at " + hour + "hr" + min + " min");
        //Set time
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);

        stopReceiver(context, cls);

        if (setCalendar.before(Calendar.getInstance())) {
            Log.i("scheduledNotification", "setReminder: is before");
            setCalendar.add(Calendar.DATE, 1);
        }

        // Enable receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (am != null)
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }



    public static void stopReceiver(Context context, Class<?> cls) {
        Log.i("scheduledNotification", "stopReceiver: ");
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (am != null)
            am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void notify(Context context) {
        Log.i("scheduledNotification", "notify: ");
        triggerNotification(context);
        TimeData notificationData = new TimeData(context);
        notificationData.setStopForever(true);
        stopReceiver(context, AlarmReceiver.class);
    }

    private static void triggerNotification(Context context) {
        Log.i("scheduledNotification", "triggerNotification: ");
        Toast.makeText(context, "Reminder", Toast.LENGTH_SHORT).show();
    }


}
