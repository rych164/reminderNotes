package com.example.remindernotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindernotes.data.model.Note
import com.example.remindernotes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository): ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { noteList -> _notes.value = noteList }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch { repository.insertNote(note) }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch { repository.updateNote(note) }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { repository.deleteNote(note) }
    }
}