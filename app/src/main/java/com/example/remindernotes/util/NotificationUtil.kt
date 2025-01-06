package com.example.remindernotes.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.remindernotes.data.model.Note

//fun scheduleReminderNotification(context: Context, note: Note) {
//    val reminderTime = note.reminderDate ?: return
//
//    val intent = Intent(context, NotificationReceiver::class.java).apply {
//        putExtra("noteTitle", note.title)
//        putExtra("noteDesciption", note.description)
//    }
//
//    val pendingIntent = PendingIntent.getBroadcast(
//        context,
//        note.id,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    alarmManager.setExactAndAllowWhileIdle(
//        AlarmManager.RTC_WAKEUP,
//        reminderTime,
//        pendingIntent
//    )
//}