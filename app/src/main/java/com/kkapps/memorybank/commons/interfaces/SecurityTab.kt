package com.kkapps.memorybank.commons.interfaces

import com.kkapps.memorybank.commons.views.MyScrollView

interface SecurityTab {
    fun initTab(requiredHash: String, listener: HashListener, scrollView: MyScrollView)

    fun visibilityChanged(isVisible: Boolean)
}
