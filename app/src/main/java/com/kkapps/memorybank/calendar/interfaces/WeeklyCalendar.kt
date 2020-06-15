package com.kkapps.memorybank.calendar.interfaces

import com.kkapps.memorybank.calendar.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
