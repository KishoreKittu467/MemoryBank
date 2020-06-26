package com.kkapps.memorybank.demos.bubbleheads

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkapps.memorybank.R
import com.kkapps.memorybank.bubbleheads.OverlapRecyclerViewAnimation
import com.kkapps.memorybank.bubbleheads.OverlapRecyclerViewClickListener
import kotlinx.android.synthetic.main.activity_overlap_image_activity.*

class OverlapImageViewOverlapActivity : AppCompatActivity(), OverlapRecyclerViewClickListener {

    //------limit number of visibleItems to be overlapped
    private val numberOfItemToBeOverlapped = 10

    //------set value of item overlapping
    private val overlapWidth = -12f

    //------init RecyclerView adapter
    private val adapter by lazy {
        RecyclerViewAdapter(numberOfItemToBeOverlapped,
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        overlapWidth,
                        resources.displayMetrics
                ).toInt()
        )
    }

    val animationList = arrayListOf(
        OverlapRecyclerViewAnimation.BOTTOM_UP,
            OverlapRecyclerViewAnimation.TOP_BOTTOM,
            OverlapRecyclerViewAnimation.LEFT_RIGHT,
            OverlapRecyclerViewAnimation.RIGHT_LEFT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overlap_image_activity)

        val mSpinnerAdapter = CustomSpinnerAdapter(this, R.layout.row_overlap_spinner_item,
                R.id.tvTitle, animationList)
        //select animation type
        spChooseAnimation.adapter = mSpinnerAdapter

        spChooseAnimation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                adapter.animationType = animationList[position]
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spChooseDirection.adapter = ArrayAdapter<String>(this, R.layout.row_overlap_spinner_item, R.id.tvTitle, arrayListOf("Left to right", "Right to left"))

        spChooseDirection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val layoutManager = LinearLayoutManager(this@OverlapImageViewOverlapActivity,
                        LinearLayoutManager.HORIZONTAL, position != 0)
                recyclerView.layoutManager = layoutManager
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        recyclerView.addItemDecoration(adapter.getItemDecoration())
        recyclerView.adapter = adapter
        adapter.addAnimation = true
        adapter.animationType = OverlapRecyclerViewAnimation.BOTTOM_UP
        adapter.addAll(getDummyArrayList())
        adapter.overlapRecyclerViewClickListener = this
    }

    /**
     * addItem dummy data to ArrayList
     */
    private fun getDummyArrayList(): java.util.ArrayList<OverlapImageModel> {
        val items = java.util.ArrayList<OverlapImageModel>()

        //-----fill data in to array list
        for (i in 0..20) {
            items.add(OverlapImageModel(imageURLs[i % imageURLs.size]))
        }
        return items
    }

    override fun onNormalItemClicked(adapterPosition: Int) {
        toast(this, "Normal item clicked >> $adapterPosition")
    }

    override fun onNumberedItemClick(adapterPosition: Int) {
        toast(this, "Numbered item clicked >> $adapterPosition")
    }

}
