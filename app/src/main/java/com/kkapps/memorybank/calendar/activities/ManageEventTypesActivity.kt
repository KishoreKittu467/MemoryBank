package com.kkapps.memorybank.calendar.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.kkapps.memorybank.R
import com.kkapps.memorybank.calendar.adapters.ManageEventTypesAdapter
import com.kkapps.memorybank.calendar.dialogs.EditEventTypeDialog
import com.kkapps.memorybank.calendar.extensions.eventsHelper
import com.kkapps.memorybank.calendar.interfaces.DeleteEventTypesListener
import com.kkapps.memorybank.calendar.models.EventType
import com.kkapps.memorybank.commons.extensions.toast
import com.kkapps.memorybank.commons.extensions.updateTextColors
import com.kkapps.memorybank.commons.helpers.ensureBackgroundThread
import kotlinx.android.synthetic.main.activity_manage_event_types.*
import java.util.*

class ManageEventTypesActivity : SimpleActivity(), DeleteEventTypesListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_event_types)

        getEventTypes()
        updateTextColors(manage_event_types_list)
    }

    private fun showEventTypeDialog(eventType: EventType? = null) {
        EditEventTypeDialog(this, eventType?.copy()) {
            getEventTypes()
        }
    }

    private fun getEventTypes() {
        eventsHelper.getEventTypes(this, false) {
            val adapter = ManageEventTypesAdapter(this, it, this, manage_event_types_list) {
                showEventTypeDialog(it as EventType)
            }
            manage_event_types_list.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_types, menu)
        updateMenuItemColors(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_event_type -> showEventTypeDialog()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean {
        if (eventTypes.any { it.caldavCalendarId != 0 }) {
            toast(R.string.unsync_caldav_calendar)
            if (eventTypes.size == 1) {
                return false
            }
        }

        ensureBackgroundThread {
            eventsHelper.deleteEventTypes(eventTypes, deleteEvents)
        }
        return true
    }
}
