package com.kkapps.memorybank.calendar.extensions

import com.kkapps.memorybank.calendar.helpers.Formatter
import com.kkapps.memorybank.calendar.models.Event

fun Long.isTsOnProperDay(event: Event): Boolean {
    val dateTime = Formatter.getDateTimeFromTS(this)
    val power = Math.pow(2.0, (dateTime.dayOfWeek - 1).toDouble()).toInt()
    return event.repeatRule and power != 0
}
