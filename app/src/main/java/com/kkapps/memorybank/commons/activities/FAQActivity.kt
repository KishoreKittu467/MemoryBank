package com.kkapps.memorybank.commons.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import com.kkapps.memorybank.R
import com.kkapps.memorybank.commons.extensions.baseConfig
import com.kkapps.memorybank.commons.extensions.getAdjustedPrimaryColor
import com.kkapps.memorybank.commons.extensions.underlineText
import com.kkapps.memorybank.commons.helpers.APP_FAQ
import com.kkapps.memorybank.commons.helpers.APP_ICON_IDS
import com.kkapps.memorybank.commons.helpers.APP_LAUNCHER_NAME
import com.kkapps.memorybank.commons.models.FAQItem
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.license_faq_item.view.*
import java.util.*

class FAQActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList()

    override fun getAppLauncherName() = intent.getStringExtra(APP_LAUNCHER_NAME) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        val titleColor = getAdjustedPrimaryColor()
        val textColor = baseConfig.textColor

        val inflater = LayoutInflater.from(this)
        val faqItems = intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem>
        faqItems.forEach {
            val faqItem = it
            inflater.inflate(R.layout.license_faq_item, null).apply {
                license_faq_title.apply {
                    text = if (faqItem.title is Int) getString(faqItem.title) else faqItem.title as String
                    underlineText()
                    setTextColor(titleColor)
                }

                license_faq_text.apply {
                    text = if (faqItem.text is Int) getString(faqItem.text) else faqItem.text as String
                    setTextColor(textColor)
                }
                faq_holder.addView(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        updateMenuItemColors(menu)
        return super.onCreateOptionsMenu(menu)
    }
}
