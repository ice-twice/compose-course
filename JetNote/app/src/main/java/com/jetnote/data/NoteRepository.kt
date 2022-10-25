package com.jetnote.data

import com.jetnote.data.db.NoteDatabaseDao
import com.jetnote.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabaseDao: NoteDatabaseDao) {

    suspend fun addNote(note: Note) = noteDatabaseDao.insertNote(note)

    suspend fun removeNote(note: Note) = noteDatabaseDao.removeNote(note)

    fun getAllNotes(): Flow<List<Note>> = noteDatabaseDao.getNotes().conflate()
}