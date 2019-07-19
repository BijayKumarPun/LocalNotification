package io.test.ntf;

import android.app.AlarmManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String TAG = "scheduledNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TimeData notificationData = new TimeData(this);
        Log.i(TAG, "onCreate: start");
        if (!notificationData.isStoppedForever()) {
            Log.i(TAG, "onCreate: MainActivity notification not stopped forever!");
            ScheduledNotifier.setReminder(this, AlarmReceiver.class, notificationData.getHr(), notificationData.getMin());
        }

    }
}
