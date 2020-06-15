package com.kkapps.memorybank.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kkapps.memorybank.calendar.extensions.config
import com.kkapps.memorybank.calendar.extensions.recheckCalDAVCalendars
import com.kkapps.memorybank.calendar.extensions.refreshCalDAVCalendars
import com.kkapps.memorybank.calendar.extensions.updateWidgets

class CalDAVSyncReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (context.config.caldavSync) {
            context.refreshCalDAVCalendars(context.config.caldavSyncedCalendarIds, false)
        }

        context.recheckCalDAVCalendars {
            context.updateWidgets()
        }
    }
}
