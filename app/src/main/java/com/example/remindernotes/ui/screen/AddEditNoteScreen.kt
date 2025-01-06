package com.example.remindernotes.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindernotes.ui.viewmodel.NotesViewModel
import com.example.remindernotes.data.model.Note
import com.example.remindernotes.util.scheduleReminderNotification

@Composable
fun AddEditNoteScreen(
    notesViewModel: NotesViewModel,
    noteToEdit: Note? = null,
    onNoteSaved: () -> Unit
) {
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var desciption by remember { mutableStateOf(noteToEdit?.description ?: "") }
    var reminderDate by remember { mutableStateOf(noteToEdit?.reminderDate ?: 0L) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tytuł notatki") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = desciption,
            onValueChange = { desciption = it },
            label = { Text("Treść notatki") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newNote = Note(
                    id = noteToEdit?.id ?: 0,
                    title = title,
                    description = desciption,
                    reminderDate = if (reminderDate > 0) reminderDate else null
                )

                if (noteToEdit == null) {
                    notesViewModel.insertNote(newNote)
                } else {
                    notesViewModel.updateNote(newNote)
                }

                if (newNote.reminderDate != null) {
                    scheduleReminderNotification(context, newNote)
                }

                onNoteSaved()
            }
        ) {
            Text("Zapisz notatkę")
        }
    }
}