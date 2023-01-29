package com.mib.feature_home.utils

import com.mib.lib_util.DateConstant.MONTH_YEAR_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Date.truncateDay(): Date {
    val calendar = Calendar.getInstance().also {
        it.time = this
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

fun Date.formatByMonthDayTime(): String {
    val dateFormat = SimpleDateFormat("MMM dd, hh.mm a", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.formatByMonthYear(): String {
    val dateFormat = SimpleDateFormat(MONTH_YEAR_FORMAT, Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.formatByDateMonthYear(): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.diffDays(anotherDate: Date): Long {
    val millisecondDifference = this.time - anotherDate.time
    return TimeUnit.DAYS.convert(millisecondDifference, TimeUnit.MILLISECONDS)
}

fun Date.diffHours(anotherDate: Date): Long {
    val millisecondDifference = this.time - anotherDate.time
    return TimeUnit.HOURS.convert(millisecondDifference, TimeUnit.MILLISECONDS)
}

fun Date.diffMinutes(anotherDate: Date): Long {
    val millisecondDifference = this.time - anotherDate.time
    return TimeUnit.MINUTES.convert(millisecondDifference, TimeUnit.MILLISECONDS)
}