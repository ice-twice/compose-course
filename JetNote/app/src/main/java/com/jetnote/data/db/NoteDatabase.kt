package com.jetnote.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jetnote.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(NoteDataConverters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDatabaseDao

    companion object {
        private const val DB_NAME = "note_database"

        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}