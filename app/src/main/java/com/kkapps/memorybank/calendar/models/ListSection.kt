package com.kkapps.memorybank.calendar.models

data class ListSection(
    val title: String,
    val code: String,
    val isToday: Boolean,
    val isPastSection: Boolean
) : ListItem()
