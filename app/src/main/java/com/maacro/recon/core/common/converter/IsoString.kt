package com.maacro.recon.core.common.converter

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String?.isoDisplay(): String {
    if (this.isNullOrBlank()) return ""
    val localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    return localDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}

fun String?.isoToMillis() = this?.let {
    LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}