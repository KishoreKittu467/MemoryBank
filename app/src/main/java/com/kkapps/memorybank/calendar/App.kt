package com.kkapps.memorybank.calendar

import androidx.multidex.MultiDexApplication
import com.kkapps.commons.extensions.checkUseEnglish

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }
}
