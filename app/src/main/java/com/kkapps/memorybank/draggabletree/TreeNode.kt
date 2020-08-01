package com.kkapps.memorybank.draggabletree

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import java.util.*

class TreeNode {
    private var level = 0
    var isRoot = false
        private set
    var parent: TreeNode? = null
        private set
    var isCollapsed = false
        private set
    val children = mutableListOf<TreeNode>()
    var data: Any? = null
        private set
    lateinit var view: View

    constructor(context: Context?) {
        isRoot = true
        view = LinearLayout(context)
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        (view as LinearLayout).orientation = LinearLayout.VERTICAL
    }

    constructor(data: Any?) {
        this.data = data
    }

    constructor(data: Any?, parent: TreeNode) {
        this.data = data
        this.parent = parent
        level = parent.level + 1
    }

    fun setExpanded(expanded: Boolean) {
        isCollapsed = !expanded
    }

    fun getLevel(): Int {
        return countParent()
    }

    private fun countParent(): Int {
        return if (parent != null) {
            parent!!.countParent() + 1
        } else {
            0
        }
    }

    val position: Int
        get() = if (parent != null) {
            parent!!.children.indexOf(this)
        } else {
            -1
        }

    fun setParent(
        parent: TreeNode?,
        pos: Int = 0
    ): TreeNode {
        var position = pos
        if (this.parent != null) {
            if (this.parent === parent) {
                if (this.parent!!.children.indexOf(this) > position) {
                    position += 1
                }
            }
            this.parent!!.removeChild(this)
        }
        this.parent = parent
        level = parent?.level?:0 + 1
        parent?.addChild(this, position)
        return this
    }

    fun setParent(parent: TreeNode): TreeNode {
        if (this.parent != null) {
            this.parent!!.removeChild(this)
        }
        this.parent = parent
        parent.addChild(this)
        return this
    }

    fun addChild(node: TreeNode) {
        if (node.parent != null) {
            node.parent!!.removeChild(node)
        }
        node.parent = this
        children.add(node)
    }

    fun addChild(node: TreeNode, position: Int) {
        var pos = position
        if (node.parent != null) {
            node.parent!!.removeChild(node)
        }
        node.parent = this
        if (pos <= -1) {
            pos = 0
        }
        children.add(pos, node)
    }

    val childLevel: Int
        get() = childLevelRecursive - 1

    private val childLevelRecursive: Int
        get() {
            var temp = 0
            for (i in children.indices) {
                if (temp < children[i].childLevelRecursive) {
                    temp = children[i].childLevelRecursive
                }
            }
            return temp + 1
        }

    fun removeChild(node: TreeNode?): Boolean {
        return children.remove(node)
    }
}
