package com.kkapps.commons.dialogs

import android.app.Activity
import android.content.ActivityNotFoundException
import com.kkapps.commons.R
import com.kkapps.commons.extensions.getStoreUrl
import com.kkapps.commons.extensions.launchViewIntent

class RateUsDialog(val activity: Activity) {

    init {
        ConfirmationDialog(activity, "", R.string.rate_us_prompt, R.string.rate, R.string.cancel) {
            launchGooglePlay()
        }
    }

    private fun launchGooglePlay() {
        try {
            activity.launchViewIntent("market://details?id=${activity.packageName.removeSuffix(".debug")}")
        } catch (ignored: ActivityNotFoundException) {
            activity.launchViewIntent(activity.getStoreUrl())
        }
    }
}
