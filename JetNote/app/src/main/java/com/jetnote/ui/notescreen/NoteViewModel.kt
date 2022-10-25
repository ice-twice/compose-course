package com.jetnote.ui.notescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetnote.data.NoteRepository
import com.jetnote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    val noteList = MutableStateFlow<List<Note>>(emptyList())

    init {
        noteRepository.getAllNotes()
            .distinctUntilChanged()
            .onEach { notes -> noteList.update { notes } }
            .launchIn(viewModelScope)
    }

    fun addNote(note: Note) = viewModelScope.launch { noteRepository.addNote(note) }

    fun removeNote(note: Note) = viewModelScope.launch { noteRepository.removeNote(note) }
}