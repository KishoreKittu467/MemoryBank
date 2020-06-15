package com.kkapps.memorybank.calendar.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.kkapps.memorybank.calendar.adapters.EventListWidgetAdapterEmpty

class WidgetServiceEmpty : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapterEmpty(applicationContext)
}
