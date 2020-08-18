package com.kkapps.commons.dialogs

import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.kkapps.commons.R
import com.kkapps.commons.activities.BaseSimpleActivity
import com.kkapps.commons.adapters.RenameAdapter
import com.kkapps.commons.extensions.baseConfig
import com.kkapps.commons.extensions.onPageChangeListener
import com.kkapps.commons.extensions.onTabSelectionChanged
import com.kkapps.commons.extensions.setupDialogStuff
import com.kkapps.commons.helpers.RENAME_PATTERN
import com.kkapps.commons.helpers.RENAME_SIMPLE
import com.kkapps.commons.views.MyViewPager
import kotlinx.android.synthetic.main.dialog_rename.view.*
import java.util.*

class RenameDialog(val activity: BaseSimpleActivity, val paths: ArrayList<String>, val useMediaFileExtension: Boolean, val callback: () -> Unit) {
    var dialog: AlertDialog? = null
    val view = LayoutInflater.from(activity).inflate(R.layout.dialog_rename, null)
    var tabsAdapter: RenameAdapter
    var viewPager: MyViewPager

    init {
        view.apply {
            viewPager = findViewById(R.id.dialog_tab_view_pager)
            tabsAdapter = RenameAdapter(activity, paths)
            viewPager.adapter = tabsAdapter
            viewPager.onPageChangeListener {
                dialog_tab_layout.getTabAt(it)!!.select()
            }
            viewPager.currentItem = activity.baseConfig.lastRenameUsed

            val textColor = context.baseConfig.textColor
            dialog_tab_layout.setTabTextColors(textColor, textColor)
            dialog_tab_layout.setSelectedTabIndicatorColor(context.baseConfig.primaryColor)

            dialog_tab_layout.onTabSelectionChanged(tabSelectedAction = {
                viewPager.currentItem = when {
                    it.text.toString().equals(resources.getString(R.string.simple_renaming), true) -> RENAME_SIMPLE
                    else -> RENAME_PATTERN
                }
            })
        }

        dialog = AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel) { dialog, which -> dismissDialog() }
                .create().apply {
                    activity.setupDialogStuff(view, this).apply {
                        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            tabsAdapter.dialogConfirmed(useMediaFileExtension, viewPager.currentItem) {
                                dismissDialog()
                                if (it) {
                                    activity.baseConfig.lastRenameUsed = viewPager.currentItem
                                    callback()
                                }
                            }
                        }
                    }
                }
    }

    private fun dismissDialog() {
        dialog!!.dismiss()
    }
}