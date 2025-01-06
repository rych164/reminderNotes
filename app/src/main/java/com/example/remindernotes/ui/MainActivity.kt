package com.example.remindernotes.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.remindernotes.data.db.AppDatabase
import com.example.remindernotes.data.repository.NoteRepository
import com.example.remindernotes.ui.screen.MainScreen
import com.example.remindernotes.ui.theme.ReminderNotesTheme
import com.example.remindernotes.ui.viewmodel.NotesViewModel
import com.example.remindernotes.ui.viewmodel.NotesViewModelFactory
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel: NotesViewModel

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
        }

    private val exactAlarmPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "notes_channel_id",
            "Przypomnienia notatek",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Kanał do powiadomień o notatkach"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    private fun checkAndRequestExactAlarmPermission() {
        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            AlertDialog.Builder(this)
                .setTitle("Potrzebne uprawnienie")
                .setMessage("Applikacja potrzebuje uprawnień dokładnych alarmów, aby przypomnienia działy poprawnie. Włącz je w ustawieniach")
                .setPositiveButton("Otwórz ustawienia") { dialog, which ->
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    exactAlarmPermissionsLauncher.launch(intent)
                }
                .setNegativeButton("anuluj") { dialog, which ->

                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        checkAndRequestExactAlarmPermission()

        val db = AppDatabase.getDatabase(this)
        val repository = NoteRepository(db.noteDao())

        notesViewModel = ViewModelProvider(
            this, NotesViewModelFactory(repository)
        )[NotesViewModel::class.java]

        setContent {
            ReminderNotesTheme {
                MainScreen(notesViewModel)
            }
        }
    }
}