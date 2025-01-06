package com.example.remindernotes.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Delete
import com.example.remindernotes.data.model.Note
import com.example.remindernotes.ui.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NotesViewModel,
    onAddClicked: () -> Unit,
    onEditClicked: (Note) -> Unit
) {
    val noteList by viewModel.notes.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Moje Notatki") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(noteList) { note ->
                NoteItem(
                    note,
                    onDelete = { viewModel.deleteNote(note) },
                    onEdit = { onEditClicked(note) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                OutlinedButton(onClick = onEdit) {
                    Text("Edytuj")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onDelete) {
                    Text("Usuń")
                }
            }
        }
    }
}

