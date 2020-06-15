package com.kkapps.memorybank.calendar.extensions

import com.kkapps.memorybank.calendar.helpers.MONTH
import com.kkapps.memorybank.calendar.helpers.WEEK
import com.kkapps.memorybank.calendar.helpers.YEAR

fun Int.isXWeeklyRepetition() = this != 0 && this % WEEK == 0

fun Int.isXMonthlyRepetition() = this != 0 && this % MONTH == 0

fun Int.isXYearlyRepetition() = this != 0 && this % YEAR == 0
