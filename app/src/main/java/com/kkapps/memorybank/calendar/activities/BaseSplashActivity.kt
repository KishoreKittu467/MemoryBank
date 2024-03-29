package com.kkapps.memorybank.calendar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kkapps.memorybank.commons.extensions.baseConfig
import com.kkapps.memorybank.commons.extensions.checkAppSideloading
import com.kkapps.memorybank.commons.extensions.isThankYouInstalled
import com.kkapps.memorybank.commons.extensions.showSideloadingDialog
import com.kkapps.memorybank.commons.extensions.getSharedTheme
import com.kkapps.memorybank.commons.extensions.checkAppIconColor
import com.kkapps.memorybank.commons.helpers.SIDELOADING_TRUE
import com.kkapps.memorybank.commons.helpers.SIDELOADING_UNCHECKED

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
