package com.jetnote.data.db

import androidx.room.TypeConverter
import java.util.*

class NoteDataConverters {

    @TypeConverter
    fun stringFromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun uuidFromString(uuid: String): UUID = UUID.fromString(uuid)

    @TypeConverter
    fun timeStampFromDate(date: Date): Long = date.time

    @TypeConverter
    fun dateFromTimeStamp(timestamp: Long): Date = Date(timestamp)
}