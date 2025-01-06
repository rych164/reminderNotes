package com.example.remindernotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remindernotes.data.db.AppDatabase
import com.example.remindernotes.data.repository.NoteRepository
import com.example.remindernotes.ui.screen.NoteListScreen
import com.example.remindernotes.ui.theme.ReminderNotesTheme
import com.example.remindernotes.ui.viewmodel.NotesViewModel
import com.example.remindernotes.ui.viewmodel.NotesViewModelFactory
import android.os.Build
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.remindernotes.ui.screen.MainScreen

class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel: NotesViewModel

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
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