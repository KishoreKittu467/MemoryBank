package com.kkapps.memorybank.calendar.models

data class EventRepetition(
    val repeatInterval: Int,
    val repeatRule: Int,
    val repeatLimit: Long
)
