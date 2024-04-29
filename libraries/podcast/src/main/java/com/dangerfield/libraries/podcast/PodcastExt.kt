package com.dangerfield.libraries.podcast

import podawan.core.Catching
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.asLocalDateTime(): LocalDateTime? {
    val dateFormats = listOf(
        DateTimeFormatter.RFC_1123_DATE_TIME, // Common in RSS feeds
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz") // Example for different locales
    )

    val dateTime = dateFormats.firstNotNullOfOrNull { format ->
        Catching { LocalDateTime.parse(this, format) }.getOrNull()
    }

    return dateTime
}