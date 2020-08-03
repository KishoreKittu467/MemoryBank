package com.kkapps.memorybank.calendar.helpers

import android.content.Context
import com.kkapps.memorybank.calendar.extensions.eventsHelper
import com.kkapps.memorybank.calendar.interfaces.WeeklyCalendar
import com.kkapps.memorybank.calendar.models.Event
import com.kkapps.commons.helpers.WEEK_SECONDS
import java.util.*

class WeeklyCalendarImpl(val callback: WeeklyCalendar, val context: Context) {
    private var mEvents = ArrayList<Event>()

    fun updateWeeklyCalendar(weekStartTS: Long) {
        val endTS = weekStartTS + 2 * WEEK_SECONDS
        context.eventsHelper.getEvents(weekStartTS, endTS) {
            mEvents = it
            callback.updateWeeklyCalendar(it)
        }
    }
}
