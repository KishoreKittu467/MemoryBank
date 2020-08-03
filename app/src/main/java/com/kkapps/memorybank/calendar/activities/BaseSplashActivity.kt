package com.kkapps.memorybank.calendar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kkapps.commons.extensions.baseConfig
import com.kkapps.commons.extensions.checkAppSideloading
import com.kkapps.commons.extensions.isThankYouInstalled
import com.kkapps.commons.extensions.showSideloadingDialog
import com.kkapps.commons.extensions.getSharedTheme
import com.kkapps.commons.extensions.checkAppIconColor
import com.kkapps.commons.helpers.SIDELOADING_TRUE
import com.kkapps.commons.helpers.SIDELOADING_UNCHECKED

abstract class BaseSplashActivity : AppCompatActivity() {
    abstract fun initActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (baseConfig.appSideloadingStatus == SIDELOADING_UNCHECKED) {
            if (checkAppSideloading()) {
                return
            }
        } else if (baseConfig.appSideloadingStatus == SIDELOADING_TRUE) {
            showSideloadingDialog()
            return
        }

        if (isThankYouInstalled()) {
            getSharedTheme {
                if (it != null) {
                    baseConfig.apply {
                        wasSharedThemeForced = true
                        isUsingSharedTheme = true
                        wasSharedThemeEverActivated = true

                        textColor = it.textColor
                        backgroundColor = it.backgroundColor
                        primaryColor = it.primaryColor
                        navigationBarColor = it.navigationBarColor
                    }

                    if (baseConfig.appIconColor != it.appIconColor) {
                        baseConfig.appIconColor = it.appIconColor
                        checkAppIconColor()
                    }
                }
                initActivity()
            }
        } else {
            initActivity()
        }
    }
}
