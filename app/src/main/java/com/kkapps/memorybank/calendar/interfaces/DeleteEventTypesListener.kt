package com.kkapps.memorybank.calendar.interfaces

import com.kkapps.memorybank.calendar.models.EventType
import java.util.*

interface DeleteEventTypesListener {
    fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean
}
