package com.kkapps.memorybank.calendar.services

import android.app.IntentService
import android.content.Intent
import com.kkapps.memorybank.calendar.extensions.config
import com.kkapps.memorybank.calendar.extensions.eventsDB
import com.kkapps.memorybank.calendar.extensions.rescheduleReminder
import com.kkapps.memorybank.calendar.helpers.EVENT_ID

class SnoozeService : IntentService("Snooze") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val eventId = intent.getLongExtra(EVENT_ID, 0L)
            val event = eventsDB.getEventWithId(eventId)
            rescheduleReminder(event, config.snoozeTime)
        }
    }
}
