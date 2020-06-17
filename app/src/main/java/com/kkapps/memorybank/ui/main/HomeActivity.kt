package com.kkapps.memorybank.ui.main

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import com.kkapps.memorybank.R
import kotlinx.android.synthetic.main.activity_main.viewPager
import kotlinx.android.synthetic.main.activity_main.fab

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Hi There!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }
}