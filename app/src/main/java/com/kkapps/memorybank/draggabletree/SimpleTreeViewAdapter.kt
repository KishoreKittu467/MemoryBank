package com.kkapps.memorybank.draggabletree

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kkapps.memorybank.R

class SimpleTreeViewAdapter(
    context: Context?,
    root: TreeNode?
) : TreeViewAdapter(context, root) {
    override fun createTreeView(
        context: Context?,
        node: TreeNode?,
        data: Any?,
        level: Int,
        hasChildren: Boolean
    ): View {
        val view =
            View.inflate(context, R.layout.tree_view_item, null)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        val textView = view.findViewById<View>(R.id.textView) as TextView
        textView.text = data as String
        return view
    }
}