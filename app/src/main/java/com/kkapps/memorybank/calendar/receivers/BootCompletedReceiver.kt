package com.kkapps.memorybank.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kkapps.memorybank.calendar.extensions.notifyRunningEvents
import com.kkapps.memorybank.calendar.extensions.recheckCalDAVCalendars
import com.kkapps.memorybank.calendar.extensions.scheduleAllEvents
import com.kkapps.commons.helpers.ensureBackgroundThread

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            context.apply {
                scheduleAllEvents()
                notifyRunningEvents()
                recheckCalDAVCalendars {}
            }
        }
    }
}
