package com.kkapps.memorybank.draggabletree

import android.content.Context
import android.view.View
import com.kkapps.memorybank.R

abstract class TreeViewAdapter(
    var context: Context?,
    var root: TreeNode?
) {
    var placeholder: View = View.inflate(context, R.layout.tree_view_item_placeholder, null)
    var badPlaceholder: View = View.inflate(context, R.layout.tree_view_item_bad_placeholder, null)

    var draggableTreeView: DraggableTreeView? = null

    fun setTreeViews() {
        val children = root?.children
        children?.let {
            for (i in it.indices) {
                var hasChildren = false
                if (children[i].children.size != 0) {
                    hasChildren = true
                }
                children[i].view = createTreeView(
                    context,
                    children[i],
                    children[i].data,
                    children[i].getLevel(),
                    hasChildren
                )
                setTreeNodeView(children[i])
            }
        }
    }

    fun setTreeNodeView(node: TreeNode) {
        val children =
            node.children
        for (i in children.indices) {
            var hasChildren = false
            if (children[i].children.size != 0) {
                hasChildren = true
            }
            children[i].view = createTreeView(
                context,
                children[i],
                children[i].data,
                children[i].getLevel(),
                hasChildren
            )
            setTreeNodeView(children[i])
        }
    }

    abstract fun createTreeView(
        context: Context?,
        node: TreeNode?,
        data: Any?,
        level: Int,
        hasChildren: Boolean
    ): View
}
