package com.example.quoteapp.Utils

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.quoteapp.Activity.ReminderActivity
import com.example.quoteapp.R

class ReminderReciver: BroadcastReceiver() {
    companion object{
        var NOTIFICATION_ID = (Math.random() * 9000).toInt() + 1000
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }else{
            showNotification(context)
        }

    }
    private fun showNotification(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            val notificationBuilder = NotificationCompat.Builder(context, ReminderActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.calender_icon)
                .setContentTitle("Reminder")
                .setContentText("Time UP $NOTIFICATION_ID")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }


}