package com.kkapps.draggabletreeview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import java.util.*

class DraggableTreeView : FrameLayout {
    private var mRootLayout: ScrollView? = null
    private var mParentLayout: LinearLayout? = null
    private var adapter: TreeViewAdapter? = null
    private var mHoverCell: BitmapDrawable? = null
    private var mHoverCellCurrentBounds: Rect? = null
    private var mHoverCellOriginalBounds: Rect? = null
    private var mobileNode: TreeNode? = null
    private var lastNode: TreeNode? = null
    private val sideMargin = 20

    enum class Drop {
        above_sibling, below_sibling, child, cancel
    }

    private var dropItem: Drop? = null
    private var mPlaceholderCheck = System.currentTimeMillis()
    private val mPlaceholderDelay: Long = 200
    private var mDownY = -1
    private var mDownX = -1
    private var mScrollDownY = -1
    private var mLastEventX = -1
    private var mLastEventY = -1
    private var nodeOrder =
        ArrayList<TreeNode?>()
    var maxLevels = -1
    var makeSiblingAtMaxLevel = true
    private var mobileView: View? = null
    private var mCellIsMobile = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
    }

    private var mDragItemCallback: DragItemCallback = object : DragItemCallback {
        override fun onStartDrag(
            item: View?,
            node: TreeNode?
        ) {
        }

        override fun onChangedPosition(
            item: View?,
            child: TreeNode?,
            parent: TreeNode?,
            position: Int
        ) {
        }

        override fun onEndDrag(
            item: View?,
            child: TreeNode?,
            parent: TreeNode?,
            position: Int
        ) {
        }
    }

    fun setOnDragItemListener(dragItemCallback: DragItemCallback) {
        mDragItemCallback = dragItemCallback
    }

    interface DragItemCallback {
        fun onStartDrag(
            item: View?,
            node: TreeNode?
        )

        fun onChangedPosition(
            item: View?,
            child: TreeNode?,
            parent: TreeNode?,
            position: Int
        )

        fun onEndDrag(
            item: View?,
            child: TreeNode?,
            parent: TreeNode?,
            position: Int
        )
    }

    fun setAdapter(adapter: TreeViewAdapter) {
        this.adapter = adapter
        this.adapter!!.draggableTreeView = this
        adapter.setTreeViews()
        notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        adapter?.let {
            mParentLayout!!.removeAllViews()
            nodeOrder = ArrayList()
            inflateViews(it.root)
        }
    }

    private fun inflateViews(node: TreeNode?) {
        node?.let {
            if (!it.isRoot) {
                createTreeItem(it.view, it)
            } else {
                (it.view as ViewGroup).removeAllViews()
                mParentLayout!!.addView(it.view as LinearLayout)
            }
            for (i in it.children.indices) {
                inflateViews(it.children[i])
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mRootLayout = ScrollView(context)
        mParentLayout = LinearLayout(context)
        mParentLayout!!.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        mRootLayout!!.addView(mParentLayout)
        addView(mRootLayout)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val colValue = handleItemDragEvent(event)
        return colValue || super.onInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val colValue = handleItemDragEvent(event)
        return colValue || super.onTouchEvent(event)
    }

    fun handleItemDragEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.rawX.toInt()
                mDownY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mDownY == -1) {
                    mDownY = event.rawY.toInt()
                }
                if (mDownX == -1) {
                    mDownX = event.rawX.toInt()
                }
                if (mScrollDownY == -1) {
                    mScrollDownY = mRootLayout!!.scrollY
                }
                mLastEventX = event.rawX.toInt()
                mLastEventY = event.rawY.toInt()
                val deltaX = mLastEventX - mDownX
                val deltaY = mLastEventY - mDownY
                if (mCellIsMobile) {
                    val location = IntArray(2)
                    mobileView!!.getLocationOnScreen(location)
                    val root_location = IntArray(2)
                    mRootLayout!!.getLocationOnScreen(root_location)
                    val offsetX = deltaX - root_location[0]
                    val offsetY =
                        location[1] + deltaY - root_location[1] + mRootLayout!!.scrollY - mScrollDownY
                    mHoverCellCurrentBounds!!.offsetTo(
                        offsetX,
                        offsetY
                    )
                    mHoverCell!!.bounds = rotatedBounds(mHoverCellCurrentBounds, 0.0523599)
                    invalidate()
                    handleItemDrag()
                    return true
                }
            }
            MotionEvent.ACTION_UP -> touchEventsCancelled()
            MotionEvent.ACTION_CANCEL -> touchEventsCancelled()
            MotionEvent.ACTION_POINTER_UP -> {
            }
            else -> {
            }
        }
        return false
    }

    private fun handleItemDrag() {
        val layout = adapter!!.root?.view as LinearLayout
        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            val lp = view.layoutParams as LinearLayout.LayoutParams
            val viewRect = Rect(
                location[0],
                location[1],
                location[0] + view.width,
                location[1] + view.height
            )
            val outRect = Rect(
                0,
                location[1] - lp.topMargin,
                Resources.getSystem().displayMetrics.widthPixels,
                location[1] + view.height + lp.bottomMargin
            )
            if (outRect.contains(mLastEventX, mLastEventY)) {
                //set last position
                val root_location = IntArray(2)
                mRootLayout!!.getLocationOnScreen(root_location)
                if (root_location[1] > mLastEventY - dpToPx(25)) {
                    mRootLayout!!.smoothScrollBy(0, -10)
                }
                if (root_location[1] + mRootLayout!!.height < mLastEventY + dpToPx(25)) {
                    mRootLayout!!.smoothScrollBy(0, 10)
                }
                if (mLastEventY < viewRect.top + view.height / 2 && mLastEventX <= mDownX + dpToPx(
                        40
                    )
                ) {
                    //above so make sibling
                    if (mPlaceholderCheck + mPlaceholderDelay < System.currentTimeMillis()) {
                        var has_changed = false
                        if (lastNode != null && (dropItem != Drop.above_sibling || lastNode !== nodeOrder[i])
                        ) {
                            //Item has changed
                            has_changed = true
                        }
                        dropItem = Drop.above_sibling
                        lastNode = nodeOrder[i]
                        if (adapter!!.placeholder.parent != null) {
                            (adapter!!.placeholder.parent as ViewGroup).removeView(adapter!!.placeholder)
                        }
                        if (adapter!!.badPlaceholder.parent != null) {
                            (adapter!!.badPlaceholder.parent as ViewGroup).removeView(adapter!!.badPlaceholder)
                        }
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(0, 0, 0, 0)
                        adapter!!.placeholder.layoutParams = layoutParams
                        adapter!!.badPlaceholder.layoutParams = layoutParams
                        val pos = (lastNode!!.view.parent as ViewGroup).indexOfChild(
                            lastNode!!.view
                        )
                        val level = mobileNode!!.childLevel + lastNode!!.getLevel()
                        if (maxLevels != -1 && maxLevels < level) {
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.badPlaceholder, pos)
                            dropItem = Drop.cancel
                        } else {
                            if (has_changed) {
                                if (i <= nodeOrder.indexOf(mobileNode) || lastNode!!.getLevel() > mobileNode!!.getLevel()) {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position + 1
                                    )
                                } else {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position
                                    )
                                }
                            }
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.placeholder, pos)
                        }
                        mPlaceholderCheck = System.currentTimeMillis()
                    }
                } else if (mLastEventX > mDownX + dpToPx(40)) {
                    //make child
                    if (mPlaceholderCheck + mPlaceholderDelay < System.currentTimeMillis()) {
                        var temp_node: TreeNode? = null
                        var has_changed = false
                        if (lastNode != null && (dropItem != Drop.child || lastNode !== nodeOrder[i])
                        ) {
                            //Item has changed
                            has_changed = true
                            temp_node = lastNode
                        }
                        dropItem = Drop.child
                        lastNode = nodeOrder[i]
                        if (adapter!!.placeholder.parent != null) {
                            (adapter!!.placeholder.parent as ViewGroup).removeView(adapter!!.placeholder)
                        }
                        if (adapter!!.badPlaceholder.parent != null) {
                            (adapter!!.badPlaceholder.parent as ViewGroup).removeView(adapter!!.badPlaceholder)
                        }
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(dpToPx(sideMargin), 0, 0, 0)
                        adapter!!.placeholder.layoutParams = layoutParams
                        adapter!!.badPlaceholder.layoutParams = layoutParams
                        val level = mobileNode!!.childLevel + lastNode!!.getLevel() + 1
                        if (maxLevels != -1 && maxLevels < level) {
                            (lastNode!!.view as ViewGroup).addView(adapter!!.badPlaceholder)
                            dropItem = Drop.cancel
                        } else {
                            if (has_changed && temp_node != null) {
                                mDragItemCallback.onChangedPosition(
                                    mobileNode!!.view,
                                    mobileNode,
                                    temp_node,
                                    0
                                )
                            }
                            (lastNode!!.view as ViewGroup).addView(adapter!!.placeholder)
                        }
                        mPlaceholderCheck = System.currentTimeMillis()
                    }
                } else if (mLastEventY > viewRect.bottom) {
                    //below so make sibling
                    if (mPlaceholderCheck + mPlaceholderDelay < System.currentTimeMillis()) {
                        var has_changed = false
                        if (lastNode != null && (dropItem != Drop.below_sibling || lastNode !== nodeOrder[i])
                        ) {
                            //Item has changed
                            has_changed = true
                        }
                        dropItem = Drop.below_sibling
                        lastNode = nodeOrder[i]
                        if (adapter!!.placeholder.parent != null) {
                            (adapter!!.placeholder.parent as ViewGroup).removeView(adapter!!.placeholder)
                        }
                        if (adapter!!.badPlaceholder.parent != null) {
                            (adapter!!.badPlaceholder.parent as ViewGroup).removeView(adapter!!.badPlaceholder)
                        }
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(0, 0, 0, 0)
                        adapter!!.placeholder.layoutParams = layoutParams
                        adapter!!.badPlaceholder.layoutParams = layoutParams
                        val pos = (lastNode!!.view.parent as ViewGroup).indexOfChild(
                            lastNode!!.view
                        )
                        val level = mobileNode!!.childLevel + lastNode!!.getLevel()
                        if (maxLevels != -1 && maxLevels < level) {
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.badPlaceholder, pos + 1)
                            dropItem = Drop.cancel
                        } else {
                            if (has_changed) {
                                if (i <= nodeOrder.indexOf(mobileNode) || lastNode!!.getLevel() > mobileNode!!.getLevel()) {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position + 2
                                    )
                                } else {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position + 1
                                    )
                                }
                            }
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.placeholder, pos + 1)
                        }
                        mPlaceholderCheck = System.currentTimeMillis()
                    }
                } else {
                    if (mPlaceholderCheck + mPlaceholderDelay < System.currentTimeMillis()) {
                        var has_changed = false
                        if (lastNode != null && (dropItem != Drop.below_sibling || lastNode !== nodeOrder[i])
                        ) {
                            //Item has changed
                            has_changed = true
                        }
                        dropItem = Drop.below_sibling
                        lastNode = nodeOrder[i]
                        if (adapter!!.placeholder.parent != null) {
                            (adapter!!.placeholder.parent as ViewGroup).removeView(adapter!!.placeholder)
                        }
                        if (adapter!!.badPlaceholder.parent != null) {
                            (adapter!!.badPlaceholder.parent as ViewGroup).removeView(adapter!!.badPlaceholder)
                        }
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(0, 0, 0, 0)
                        adapter!!.placeholder.layoutParams = layoutParams
                        adapter!!.badPlaceholder.layoutParams = layoutParams
                        val pos = (lastNode!!.view.parent as ViewGroup).indexOfChild(
                            lastNode!!.view
                        )
                        val level = mobileNode!!.childLevel + lastNode!!.getLevel()
                        if (maxLevels != -1 && maxLevels < level) {
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.badPlaceholder, pos + 1)
                            dropItem = Drop.cancel
                        } else {
                            if (has_changed) {
                                if (i <= nodeOrder.indexOf(mobileNode) || lastNode!!.getLevel() > mobileNode!!.getLevel()) {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position + 2
                                    )
                                } else {
                                    mDragItemCallback.onChangedPosition(
                                        mobileNode!!.view,
                                        mobileNode,
                                        lastNode!!.parent,
                                        lastNode!!.position + 1
                                    )
                                }
                            }
                            (lastNode!!.view
                                .parent as ViewGroup).addView(adapter!!.placeholder, pos + 1)
                        }
                        mPlaceholderCheck = System.currentTimeMillis()
                    }
                }
            }
        }
    }

    private fun touchEventsCancelled() {
        if (mCellIsMobile && mobileNode != null) {
            mobileView!!.visibility = View.VISIBLE
            mHoverCell = null
            if (adapter != null) {
                if (adapter!!.placeholder.parent != null) {
                    (adapter!!.placeholder.parent as ViewGroup).removeView(adapter!!.placeholder)
                }
                if (adapter!!.badPlaceholder.parent != null) {
                    (adapter!!.badPlaceholder.parent as ViewGroup).removeView(adapter!!.badPlaceholder)
                }
                //Make sure it didn't drop on itself
                if (lastNode !== mobileNode || dropItem != Drop.cancel) {
                    if (dropItem == Drop.above_sibling) {
                        val pos = lastNode!!.position
                        mobileNode!!.setParent(lastNode!!.parent, pos - 1)
                        mDragItemCallback.onEndDrag(
                            mobileNode!!.view,
                            mobileNode,
                            lastNode,
                            mobileNode!!.position + 1
                        )
                    } else if (dropItem == Drop.below_sibling) {
                        val pos = lastNode?.position ?: 0
                        //if it came from below we need to add
                        mobileNode!!.setParent(lastNode?.parent, pos)
                        mDragItemCallback.onEndDrag(
                            mobileNode!!.view,
                            mobileNode,
                            lastNode,
                            mobileNode!!.position + 1
                        )
                    } else if (dropItem == Drop.child) {
                        mobileNode!!.setParent(lastNode, 0)
                        mDragItemCallback.onEndDrag(
                            mobileNode!!.view,
                            mobileNode,
                            lastNode,
                            mobileNode!!.position + 1
                        )
                    }
                }
                notifyDataSetChanged()
            }
            invalidate()
        }
        mDownX = -1
        mDownY = -1
        mScrollDownY = -1
        mCellIsMobile = false
    }

    fun createTreeItem(
        view: View?,
        node: TreeNode?
    ) {
        if (view != null) {
            nodeOrder.add(node)
            val mItem = LinearLayout(context)
            mItem.orientation = LinearLayout.VERTICAL
            if (view.parent != null) {
                val parent = view.parent as ViewGroup
                parent.removeView(view)
            }
            view.setOnLongClickListener(OnLongClickListener {
                mobileNode = node
                addToView(mItem, node)
                mobileView = mItem
                mDragItemCallback.onStartDrag(mobileNode!!.view, mobileNode)
                mItem.post {
                    mCellIsMobile = true
                    mHoverCell = getAndAddHoverView(mobileView, 1f)
                    (mobileView as LinearLayout).visibility = View.INVISIBLE
                }
                false
            })
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(dpToPx(sideMargin * (node?.getLevel() ?: 0)), 0, 0, 0)
            mItem.layoutParams = layoutParams
            mItem.addView(view)
            (adapter!!.root?.view as LinearLayout).addView(mItem)
        }
    }

    private fun addToView(
        linearLayout: LinearLayout,
        node: TreeNode?
    ) {
        node?.let {
            for (i in it.children.indices) {
                val child = it.children[i].view
                if (child.parent.parent != null) {
                    (child.parent
                        .parent as ViewGroup).removeView(child.parent as View)
                }
                linearLayout.addView(child.parent as View)
                addToView(linearLayout, it.children[i])
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mHoverCell != null) {
            mHoverCell!!.draw(canvas)
        }
    }

    private fun getAndAddHoverView(v: View?, scale: Float): BitmapDrawable {
        val w = v!!.width
        val h = v.height
        val top = v.top
        val left = v.left
        val b = getBitmapWithBorder(v, scale)
        val drawable = BitmapDrawable(resources, b)
        mHoverCellOriginalBounds = Rect(left, top, left + w, top + h)
        mHoverCellCurrentBounds = Rect(mHoverCellOriginalBounds)
        drawable.bounds = mHoverCellCurrentBounds!!
        return drawable
    }

    private fun getBitmapWithBorder(v: View?, scale: Float): Bitmap {
        val bitmap = getBitmapFromView(v, 0f)
        val b = getBitmapFromView(v, 1f)
        val can = Canvas(bitmap)
        val paint = Paint()
        paint.alpha = 150
        can.scale(scale, scale, mDownX.toFloat(), mDownY.toFloat())
        can.rotate(3f)
        can.drawBitmap(b, 0f, 0f, paint)
        return bitmap
    }

    private fun getBitmapFromView(v: View?, scale: Float): Bitmap {
        val radians = 0.0523599
        val s = Math.abs(Math.sin(radians))
        val c = Math.abs(Math.cos(radians))
        val width = (v!!.height * s + v.width * c).toInt()
        val height = (v.width * s + v.height * c).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.scale(scale, scale)
        v.draw(canvas)
        return bitmap
    }

    private fun rotatedBounds(
        tmp: Rect?,
        radians: Double
    ): Rect {
        val s = Math.abs(Math.sin(radians))
        val c = Math.abs(Math.cos(radians))
        val width = (tmp!!.height() * s + tmp.width() * c).toInt()
        val height = (tmp.width() * s + tmp.height() * c).toInt()
        return Rect(tmp.left, tmp.top, tmp.left + width, tmp.top + height)
    }
}
