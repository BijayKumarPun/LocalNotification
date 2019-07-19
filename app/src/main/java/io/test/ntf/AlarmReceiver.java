package io.test.ntf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

/**
 * This class represents ..
 * <p>
 * Created on 7/18/19 at 10:26 AM
 *
 * @author bj
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) return;
        Log.i("scheduledNotification", "onReceive: ");

        TimeData notificationData = new TimeData(context);

        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                if (!notificationData.isStoppedForever()) {
                    Log.i("scheduledNotification", "onReceive: reboot");

                    ScheduledNotifier.setReminder(context, AlarmReceiver.class, notificationData.getHr(), notificationData.getMin());
                }
                return;
            }
        }


        if (hasInternetConnection(context)) {
            ScheduledNotifier.notify(context);
        } else {
            //increase reminder time by 1hr
            //reset if it goes beyond 2200 hrs
            int hr = notificationData.getHr() + 1;

           if (hr >= TimeData.end_time) {
                hr = TimeData.start_time;
            }
            Log.i("scheduledNotification", "onReceive:no internet. Time before" + notificationData.getMin());
            notificationData.set(hr, notificationData.getMin() );
            Log.i("scheduledNotification", "onReceive: time now " + notificationData.getMin());

            //  notificationData.set(hr, notificationData.getMin());
            ScheduledNotifier.setReminder(context, AlarmReceiver.class, notificationData.getHr(), notificationData.getMin());
        }
    }


    private boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
                if (ni != null) {
                    return ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE);
                }
            } else {
                final Network n = connectivityManager.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(n);
                    if (nc != null) {

                        return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                    }
                }
                return false;

            }
            return false;
        }
        return false;
    }
}



