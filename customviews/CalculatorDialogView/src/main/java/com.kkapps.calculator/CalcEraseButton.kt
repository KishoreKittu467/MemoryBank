package com.kkapps.calculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 * ImageView that triggers erase events when held down
 * Attributes:
 * - eraseBtnHoldDelay: Time view has to be held down to trigger quick erase (in ms)
 * Default value is 750ms. Use -1 for no quick erase and 0 for no delay
 * - eraseBtnHoldSpeed: Time after which an erase event is triggered in quick erase mode (in ms)
 * Default value is 100ms
 * - eraseAllOnHold: If true, holding button will trigger an erase all event instead of quick
 * erase mode if false. By default this is false.
 */
internal class CalcEraseButton @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private val eraseHoldDelay: Int
    private var eraseHoldSpeed: Int = 100
    private var eraseAllOnHold: Boolean = false
    private var eraseHandler: Handler = Handler()
    private val eraseRunnable: Runnable by lazy {
        Runnable {
        if (listener != null && clickingDown) {
            if (eraseAllOnHold) {
                listener!!.onEraseAll()
            } else {
                listener!!.onErase()
                eraseHandler.postDelayed(
                    eraseRunnable,
                    eraseHoldSpeed.toLong()
                )
            }
        }
    }
    }
    private var clickingDown = false
    private var listener: EraseListener? = null

    interface EraseListener {
        fun onErase()
        fun onEraseAll()
    }

    fun setOnEraseListener(listener: EraseListener?) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val superReturn =
            super.onTouchEvent(event) // does performClick(), so ignore warning
        if (event.action == MotionEvent.ACTION_UP) {
            if (listener != null && eraseHoldDelay != NO_HOLD_ERASE) {
                eraseHandler.removeCallbacks(eraseRunnable)
            }
            clickingDown = false
            return true
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            clickingDown = true
            if (listener != null) {
                if (eraseHoldDelay != NO_HOLD_ERASE) {
                    eraseHandler.postDelayed(eraseRunnable, eraseHoldDelay.toLong())
                    eraseHandler.postDelayed({
                        if (clickingDown) {
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }
                    }, eraseHoldDelay.toLong())
                }
                if (eraseHoldDelay != 0) {
                    listener!!.onErase()
                }
            }
            return true
        }
        return superReturn
    }

    companion object {
        //    private static final String TAG = CalcEraseButton.class.getSimpleName();
        private const val NO_HOLD_ERASE = -1
    }

    init {
        // Get speed attributes
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalcEraseButton)
        eraseHoldDelay = ta.getInt(R.styleable.CalcEraseButton_calcEraseBtnHoldDelay, 750)
        eraseHoldSpeed = ta.getInt(R.styleable.CalcEraseButton_calcEraseBtnHoldSpeed, 100)
        eraseAllOnHold = ta.getBoolean(R.styleable.CalcEraseButton_calcEraseAllOnHold, false)
        ta.recycle()
    }
}
