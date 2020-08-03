package com.kkapps.memorybank.calendar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kkapps.memorybank.calendar.extensions.config
import com.kkapps.memorybank.calendar.extensions.eventsDB
import com.kkapps.memorybank.calendar.extensions.rescheduleReminder
import com.kkapps.memorybank.calendar.helpers.EVENT_ID
import com.kkapps.commons.extensions.showPickSecondsDialogHelper
import com.kkapps.commons.helpers.ensureBackgroundThread

class SnoozeReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showPickSecondsDialogHelper(config.snoozeTime, true, cancelCallback = { dialogCancelled() }) {
            ensureBackgroundThread {
                val eventId = intent.getLongExtra(EVENT_ID, 0L)
                val event = eventsDB.getEventWithId(eventId)
                config.snoozeTime = it / 60
                rescheduleReminder(event, it / 60)
                runOnUiThread {
                    finishActivity()
                }
            }
        }
    }

    private fun dialogCancelled() {
        finishActivity()
    }

    private fun finishActivity() {
        finish()
        overridePendingTransition(0, 0)
    }
}
