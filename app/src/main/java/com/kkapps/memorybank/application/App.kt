package com.kkapps.memorybank.application

import androidx.multidex.MultiDexApplication
import com.kkapps.commons.extensions.checkUseEnglish

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
    }
}
