package com.example.remindernotes.ui.screen

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
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
import java.util.Calendar

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

    var selectedHour by remember { mutableStateOf(10) }
    var selectedMinute by remember { mutableStateOf(0) }

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

        Button(onClick = {
            showTimePickerDialog(
                context = context,
                initialHour = selectedHour,
                initialMinute = selectedMinute
            ) {
                chosenHour, chosenMinute ->
                selectedHour = chosenHour
                selectedMinute = chosenMinute

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, chosenHour)
                    set(Calendar.MINUTE, chosenMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                reminderDate = calendar.timeInMillis
            }
        }) {
            Text("Wybierz godzinę przypomnienia")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newNote = Note(
                    id = noteToEdit?.id ?: 0,
                    title = title,
                    description = desciption,
                    reminderDate = if (reminderDate > System.currentTimeMillis()) reminderDate else null
                )

                if (noteToEdit == null) {
                    notesViewModel.insertNote(newNote)
                } else {
                    notesViewModel.updateNote(newNote)
                }

                if (canScheduleExactAlarms(context)) {
                    if (newNote.reminderDate != null) {
                        try {
                            scheduleReminderNotification(context, newNote)
                        } catch (e: SecurityException) {
                            Toast.makeText(
                                context,
                                "Brak uprawnień do ustawiania dokładnych alarmów",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Aplikacja nie ma uprawnień do ustawiania dokładnych alarmów",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                onNoteSaved()
            }
        ) {
            Text("Zapisz notatkę")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun canScheduleExactAlarms(context: Context):Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

private fun showTimePickerDialog(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        onTimeSelected(hourOfDay, minute)
    }

    val dialog = TimePickerDialog(
        context,
        timeSetListener,
        initialHour,
        initialMinute,
        true
    )

    dialog.show()
}