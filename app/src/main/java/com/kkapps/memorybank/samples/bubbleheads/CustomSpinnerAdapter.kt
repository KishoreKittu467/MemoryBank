package com.kkapps.memorybank.samples.bubbleheads

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kkapps.memorybank.bubbleheads.OverlapRecyclerViewAnimation


class CustomSpinnerAdapter(
    private val context: Activity,
    private val resourceId: Int,
    private val textViewId: Int,
    list: List<OverlapRecyclerViewAnimation>
) : ArrayAdapter<OverlapRecyclerViewAnimation>(context, resourceId, textViewId, list) {

    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false)
        }
        val rowItem = getItem(position)!!.toString()
        val txtTitle = convertView!!.findViewById<TextView>(textViewId)
        txtTitle.text = rowItem
        return convertView
    }
}
