package com.jetweatherforecast.ui.screens.main

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFormatter {
    companion object {
        fun formatDate(
            timestamp: Int,
            formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        ): String {
            val zonedDateTime =
                Instant.ofEpochSecond(timestamp.toLong()).atZone(ZoneId.systemDefault())
            return zonedDateTime.format(formatter)
        }

        fun formatDateTime(timestamp: Int): String {
            val zonedDateTime =
                Instant.ofEpochSecond(timestamp.toLong()).atZone(ZoneId.systemDefault())
            return zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
}
