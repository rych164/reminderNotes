package com.example.remindernotes.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remindernotes.data.repository.NoteRepository

class NotesViewModelFactory(
    private val repository: NoteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel( repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}