package com.example.remindernotes.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.remindernotes.data.model.Note
import java.util.Calendar

@SuppressLint("ScheduleExactAlarm")
fun scheduleReminderNotification(context: Context, note: Note) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("noteTitle", note.title)
        putExtra("noteDescription", note.description)
        putExtra("noteId", note.id)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        note.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        timeInMillis = note.reminderDate ?: 0L
    }

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun cancelReminderNotification(context: Context, note: Note) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("noteId", note.id)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        note.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(pendingIntent)
}