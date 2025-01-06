package com.example.remindernotes.ui.screen

import androidx.compose.runtime.*
import com.example.remindernotes.data.model.Note
import com.example.remindernotes.ui.viewmodel.NotesViewModel

@Composable
fun MainScreen(notesViewModel: NotesViewModel) {
    var showAddEditScreen by remember { mutableStateOf(false) }
    var editableNote by remember { mutableStateOf<Note?>(null) }

    if (showAddEditScreen) {
        AddEditNoteScreen(
            notesViewModel = notesViewModel,
            noteToEdit = editableNote,
            onNoteSaved = {
                showAddEditScreen = false
                editableNote = null
            }
        )
    } else {
        NoteListScreen(
            viewModel = notesViewModel,
            onAddClicked = {
                editableNote = null
                showAddEditScreen = true
            },
            onEditClicked = { note ->
                editableNote = note
                showAddEditScreen = true
            }
        )
    }
}