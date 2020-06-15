package com.kkapps.memorybank.calendar.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.kkapps.memorybank.calendar.adapters.EventListWidgetAdapter

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapter(applicationContext)
}
