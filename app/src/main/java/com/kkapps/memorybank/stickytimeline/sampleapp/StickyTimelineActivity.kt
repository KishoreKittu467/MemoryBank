package com.kkapps.memorybank.stickytimeline.sampleapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kkapps.memorybank.ItemDecoration.VerticalSectionItemDecoration
import com.kkapps.memorybank.R
import com.kkapps.memorybank.stickytimeline.*
import kotlinx.android.synthetic.main.sticky_timeline_activity.*

class StickyTimelineActivity : AppCompatActivity() {

    val icFinkl: Drawable? by lazy {
        AppCompatResources.getDrawable(this@StickyTimelineActivity, R.drawable.ic_finkl)
    }
    val icBuzz: Drawable? by lazy {
        AppCompatResources.getDrawable(this@StickyTimelineActivity, R.drawable.ic_buzz)
    }
    val icWannaOne: Drawable? by lazy {
        AppCompatResources.getDrawable(this@StickyTimelineActivity, R.drawable.ic_wannaone)
    }
    val icGirlsGeneration: Drawable? by lazy {
        AppCompatResources.getDrawable(this@StickyTimelineActivity, R.drawable.ic_girlsgeneration)
    }
    val icSolo: Drawable? by lazy {
        AppCompatResources.getDrawable(this@StickyTimelineActivity, R.drawable.ic_solo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sticky_timeline_activity)

        //Currently only LinearLayoutManager is supported.
        stickyRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )

        //Get data
        val singerList = getSingerList()


        //Add VerticalSectionItemDecoration.SectionCallback
        stickyRecyclerView.addItemDecoration(getSectionCallback(singerList))

        //Set Adapter
        stickyRecyclerView.adapter = SingerAdapter(
            layoutInflater,
            singerList,
            R.layout.stickey_timeline_recycler_row
        )
    }

    //Get data method
    private fun getSingerList(): List<Singer> = SingerRepo().singerList


    //Get SectionCallback method
    private fun getSectionCallback(singerList: List<Singer>): VerticalSectionItemDecoration.SectionCallback {
        return object : VerticalSectionItemDecoration.SectionCallback {
            //In your data, implement a method to determine if this is a section.
            override fun isSection(position: Int): Boolean =
                singerList[position].debuted != singerList[position - 1].debuted

            //Implement a method that returns a SectionHeader.
            override fun getSectionHeader(position: Int): SectionInfo? {
                val singer = singerList[position]
                val dot: Drawable? = when (singer.group) {
                    "FIN.K.L" -> {
                        icFinkl
                    }
                    "Girls' Generation" -> {
                        icGirlsGeneration
                    }
                    "Buzz" -> {
                        icBuzz
                    }
                    "Wanna One" -> {
                        icWannaOne
                    }
                    else -> icSolo
                }
                return SectionInfo(singer.debuted, singer.group, dot)
            }

        }
    }
}
