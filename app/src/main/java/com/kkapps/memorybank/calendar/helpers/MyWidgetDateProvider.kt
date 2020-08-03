package com.kkapps.memorybank.calendar.helpers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.kkapps.memorybank.R
import com.kkapps.memorybank.calendar.activities.SplashActivity
import com.kkapps.memorybank.calendar.extensions.config
import com.kkapps.commons.extensions.applyColorFilter
import com.kkapps.commons.extensions.getLaunchIntent
import com.kkapps.commons.extensions.setText

class MyWidgetDateProvider : AppWidgetProvider() {
    private val OPEN_APP_INTENT_ID = 1

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            RemoteViews(context.packageName, R.layout.widget_date).apply {
                applyColorFilter(R.id.widget_date_background, context.config.widgetBgColor)
                setText(R.id.widget_date_label, Formatter.getTodayDayNumber())
                setText(R.id.widget_month_label, Formatter.getCurrentMonthShort())

                setTextColor(R.id.widget_date_label, context.config.widgetTextColor)
                setTextColor(R.id.widget_month_label, context.config.widgetTextColor)

                setupAppOpenIntent(context, this)
                appWidgetManager.updateAppWidget(it, this)
            }

            appWidgetManager.notifyAppWidgetViewDataChanged(it, R.id.widget_date_holder)
        }
    }

    private fun getComponentName(context: Context) = ComponentName(context, MyWidgetDateProvider::class.java)

    private fun setupAppOpenIntent(context: Context, views: RemoteViews) {
        (context.getLaunchIntent() ?: Intent(context, SplashActivity::class.java)).apply {
            val pendingIntent = PendingIntent.getActivity(context, OPEN_APP_INTENT_ID, this, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(R.id.widget_date_holder, pendingIntent)
        }
    }
}
