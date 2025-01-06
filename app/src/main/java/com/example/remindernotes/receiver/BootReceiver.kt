package com.example.remindernotes.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindernotes.data.db.AppDatabase
import com.example.remindernotes.data.repository.NoteRepository
import com.example.remindernotes.util.scheduleReminderNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val db = AppDatabase.getDatabase(context)
            val repository = NoteRepository(db.noteDao())

            CoroutineScope(Dispatchers.IO).launch {
                val notesWithReminders = repository.getNotesWithReminders()
                notesWithReminders.forEach { note ->
                    if (note.reminderDate != null && note.reminderDate > System.currentTimeMillis()) {
                        scheduleReminderNotification(context, note)
                    }
                }
            }
        }
    }
}