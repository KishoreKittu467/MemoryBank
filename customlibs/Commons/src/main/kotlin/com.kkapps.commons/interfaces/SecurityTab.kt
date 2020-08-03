package com.kkapps.commons.interfaces

import com.kkapps.commons.views.MyScrollView

interface SecurityTab {
    fun initTab(requiredHash: String, listener: HashListener, scrollView: MyScrollView)

    fun visibilityChanged(isVisible: Boolean)
}
