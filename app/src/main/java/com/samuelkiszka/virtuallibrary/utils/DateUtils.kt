package com.samuelkiszka.virtuallibrary.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {
    private fun convertMillisToLocalDate(millis: Long) : LocalDate {
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    private fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter: DateTimeFormatter) : LocalDate {
        //Convert the date to a long in millis using a formatter
        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        //Convert the millis to a localDate object
        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    private fun dateToString(date: LocalDate): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(date, dateFormatter)
        return dateFormatter.format(dateInMillis)
    }

    fun millisToString(millis: Long): String {
        val date = convertMillisToLocalDate(millis)
        return dateToString(date)
    }

    fun millisToDays(millis: Long): Int {
        return (millis / (1000 * 60 * 60 * 24)).toInt()
    }

    fun stringToMillis(date: String): Long? {
        if (date.isEmpty()) {
            return null
        }
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.getDefault())
        val localDate = LocalDate.parse(date, dateFormatter)
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}