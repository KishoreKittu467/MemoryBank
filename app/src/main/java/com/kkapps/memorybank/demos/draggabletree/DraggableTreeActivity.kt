package com.kkapps.memorybank.demos.draggabletree

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kkapps.memorybank.R
import com.kkapps.memorybank.draggabletree.DraggableTreeView
import com.kkapps.memorybank.draggabletree.DraggableTreeView.DragItemCallback
import com.kkapps.memorybank.draggabletree.SimpleTreeViewAdapter
import com.kkapps.memorybank.draggabletree.TreeNode

class DraggableTreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_draggable_tree)
        val draggableTreeView =
            findViewById<View>(R.id.dtv) as DraggableTreeView
        val root =
            TreeNode(this)
        val item =
            TreeNode("Item 1")
        val item2 =
            TreeNode("Item 2")
        val subitem =
            TreeNode("Sub Item 2")
        subitem.addChild(TreeNode("Sub Sub Item 1"))
        item.addChild(subitem)
        item.addChild(TreeNode("Sub Item 1"))
        root.addChild(TreeNode("Item 3"))
        root.addChild(TreeNode("Item 4"))
        root.addChild(TreeNode("Item 5"))
        root.addChild(TreeNode("Item 6"))
        root.addChild(TreeNode("Item 7"))
        root.addChild(TreeNode("Item 8"))
        root.addChild(TreeNode("Item 9"))
        root.addChild(TreeNode("Item 10"))
        root.addChild(TreeNode("Item 11"))
        root.addChild(TreeNode("Item 12"))
        root.addChild(item2)
        root.addChild(item)
        draggableTreeView.setAdapter(SimpleTreeViewAdapter(this, root))
        draggableTreeView.setOnDragItemListener(object : DragItemCallback {
            override fun onStartDrag(
                item: View?,
                node: TreeNode?
            ) {
                Log.e("start", node?.data.toString())
            }

            override fun onChangedPosition(
                item: View?,
                child: TreeNode?,
                parent: TreeNode?,
                position: Int
            ) {
                Log.e(
                    "changed",
                    parent?.data.toString() + " > " + child?.data.toString() + ":" + position
                )
            }

            override fun onEndDrag(
                item: View?,
                child: TreeNode?,
                parent: TreeNode?,
                position: Int
            ) {
                Log.e(
                    "end",
                    parent?.data.toString() + " > " + child?.data.toString() + ":" + position
                )
            }
        })
    }
}