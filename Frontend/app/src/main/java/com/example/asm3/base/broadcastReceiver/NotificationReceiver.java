package com.example.asm3.base.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.asm3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//TODO: request user's permission for receiving notification when user submits an order
public class NotificationReceiver extends BroadcastReceiver {
    // broadcast
    private static final String TAG = "NOTIFICATION_BROADCAST";
    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    // notification
    public static final String CHANNEL_ID = "channel1";
    NotificationCompat.Builder builder;




    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equalsIgnoreCase(TAG)){
            try {
                // TODO: insert key for getExtras
                JSONObject newNotification = new JSONObject(intent.getExtras().getString(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendNotification(context);
        }
    }

    // Helpers
    public void sendNotification(Context context) {
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.goat_logo)
                .setContentTitle("GoGoat")
                .setContentText("You have 1 new notification!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //notificationManager.notify(1, notification);
    }
}
