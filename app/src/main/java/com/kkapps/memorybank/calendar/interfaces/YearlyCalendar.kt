package com.kkapps.memorybank.calendar.interfaces

import android.util.SparseArray
import com.kkapps.memorybank.calendar.models.DayYearly
import java.util.*

interface YearlyCalendar {
    fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int)
}
