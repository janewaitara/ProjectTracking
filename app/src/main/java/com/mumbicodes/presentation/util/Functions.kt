package com.mumbicodes.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun provideFormatter(pattern: String) = DateTimeFormatter.ofPattern(pattern)

fun convertDateToString(localDate: LocalDate, pattern: String): String {
    val formatter = provideFormatter(pattern)
    return localDate.format(formatter)
}

fun convertStringToLocalDate(dateAsString: String, pattern: String): LocalDate {
    val formatter = provideFormatter(pattern)
    return LocalDate.parse(dateAsString, formatter)
}

fun convertDateFormatsStrings(
    dateAsString: String,
    oldPattern: String,
    newPattern: String,
): String {
    val localDate = convertStringToLocalDate(dateAsString, oldPattern)
    return convertDateToString(localDate, newPattern)
}

fun convertLocalDateToLong(localDate: LocalDate): Long = localDate.toEpochDay()
fun convertLongToLocalDate(time: Long): LocalDate = LocalDate.ofEpochDay(time)

fun numberOfRemainingDays(dateAsString: String, pattern: String): Long {
    val localDate = convertStringToLocalDate(dateAsString, pattern)
    return convertLocalDateToLong(LocalDate.now()) - convertLocalDateToLong(localDate)
}

fun Long.getNumberOfDays(): Int = (this - convertLocalDateToLong(LocalDate.now())).toInt()

fun Long.toDateAsString(pattern: String): String {
    val localDate = convertLongToLocalDate(this)
    return convertDateToString(localDate, pattern)
}
