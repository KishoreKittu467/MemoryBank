package com.kkapps.memorybank.calendar.interfaces

import android.content.Context
import com.kkapps.memorybank.calendar.models.DayMonthly
import org.joda.time.DateTime

interface MonthlyCalendar {
    fun updateMonthlyCalendar(context: Context, month: String, days: ArrayList<DayMonthly>, checkedEvents: Boolean, currTargetDate: DateTime)
}
