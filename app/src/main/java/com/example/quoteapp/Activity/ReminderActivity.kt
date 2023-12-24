package com.example.quoteapp.Activity

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat.Flags
import androidx.databinding.DataBindingUtil

import com.example.quoteapp.R
import com.example.quoteapp.Utils.ReminderReciver
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityReminderBinding
import com.google.android.material.shadow.ShadowRenderer
import java.util.*

class ReminderActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "reminder_channel"

        private const val PERMISSION_REQUEST_CODE = 456
    }

    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder)
//        val sh = SharedPreferences.
        toggleButton()
        alclick()
    }
    override fun onResume() {
        super.onResume()
        checkNotificationPermission()
    }

    private fun toggleButton() {
        binding.switchButtonMorning.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.imageView4.setColorFilter(
                    ContextCompat.getColor(this, R.color.oranegshade),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.alarmTimeTextMorning.setTextColor(
                    ContextCompat.getColor(this, R.color.alarm_text_color)
                )
            } else {
                binding.imageView4.setColorFilter(
                    ContextCompat.getColor(this, R.color.linecolor),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.alarmTimeTextMorning.setTextColor(
                    ContextCompat.getColor(this, R.color.linecolor)
                )
            }
        }

        binding.switchButtonEvening.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.imageView5.setColorFilter(
                    ContextCompat.getColor(this, R.color.oranegshade),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.alarmTimeTextEvening.setTextColor(
                    ContextCompat.getColor(this, R.color.alarm_text_color)
                )
            } else {
                binding.imageView5.setColorFilter(
                    ContextCompat.getColor(this, R.color.linecolor),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.alarmTimeTextEvening.setTextColor(
                    ContextCompat.getColor(this, R.color.linecolor)
                )
            }
        }
    }

    private fun alclick() {
        binding.back.setOnClickListener {
            finish()
        }

        binding.clockMorning.setOnClickListener {
            if (binding.switchButtonMorning.isChecked) {
                timePicker(binding.alarmTimeTextMorning)
            }
        }

        binding.clockEvening.setOnClickListener {
            if (binding.switchButtonEvening.isChecked) {
                timePicker(binding.alarmTimeTextEvening)
            }
        }
    }

    private fun timePicker(view: TextView) {
        val currentTime = Calendar.getInstance()
        val isViewEmpty = view.text.isEmpty()

        val hour: Int
        val minute: Int
        val isMorning: Boolean

        if (isViewEmpty) {
            hour = currentTime.get(Calendar.HOUR_OF_DAY)
            minute = currentTime.get(Calendar.MINUTE)
            isMorning = currentTime.get(Calendar.AM_PM) == Calendar.AM
        } else {
            val timeParts = view.text.split(":", " ")
            hour = timeParts[0].toInt()
            minute = timeParts[1].toInt()
            isMorning = view.text.endsWith("AM", ignoreCase = true)
        }

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = formatTime(selectedHour, selectedMinute)
                view.text = formattedTime
                setReminder(this,selectedHour,selectedMinute)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val displayTime = dateFormat.format(calendar.time)

        val displayHour = calendar.get(Calendar.HOUR)
        val displayMinute = calendar.get(Calendar.MINUTE)

        val amPm = if (hour < 12) "AM" else "PM"

        val formattedHour = if (displayHour == 0) "12" else displayHour.toString()
        val formattedMinute = String.format(Locale.getDefault(), "%02d", displayMinute)

        return "$formattedHour:$formattedMinute $amPm"
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, "Reminder Channel", importance)
            channel.description = "Channel description"
            notificationManager.createNotificationChannel(channel)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.VIBRATE,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }



    private fun setReminder(context: Context, hourOfDay: Int, minute: Int) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReciver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val hour = if (hourOfDay > 12) hourOfDay - 12 else hourOfDay
        val amPm = if (hourOfDay >= 12) Calendar.PM else Calendar.AM

        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.AM_PM, amPm)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}
