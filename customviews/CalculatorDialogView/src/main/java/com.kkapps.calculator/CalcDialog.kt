package com.kkapps.calculator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import com.kkapps.calculator.CalcEraseButton.EraseListener
import kotlinx.android.synthetic.main.dialog_calc.view.*
import java.math.BigDecimal

/**
 * Dialog with calculator for entering and calculating a number.
 * All settings must be set before showing the dialog or unexpected behavior will occur.
 */
class CalcDialog : AppCompatDialogFragment() {
    private lateinit var contentView: View
    private var fragcontext: Context? = null
    private var presenter: CalcPresenter? = null

    /**
     * @return the calculator settings that can be changed.
     */
    var settings: CalcSettings? = CalcSettings()
        private set
    private var errorMessages: Array<CharSequence>? = null

    ////////// LIFECYCLE METHODS //////////
    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Wrap calculator dialog's theme to context
        val ta =
            context.obtainStyledAttributes(intArrayOf(R.attr.calcDialogStyle))
        val style = ta.getResourceId(0, R.style.CalcDialogStyle)
        ta.recycle()
        fragcontext = ContextThemeWrapper(context, style)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(state: Bundle?): Dialog {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_calc, null)
        val dialog = Dialog(requireContext())

        contentView.apply {
            // Get attributes
            val ta = fragcontext!!.obtainStyledAttributes(R.styleable.CalcDialog)
            val btnTexts = ta.getTextArray(R.styleable.CalcDialog_calcButtonTexts)
            errorMessages = ta.getTextArray(R.styleable.CalcDialog_calcErrors)
            val maxDialogWidth =
                ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxWidth, -1)
            val maxDialogHeight =
                ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxHeight, -1)
            val headerColor = getColor(ta, R.styleable.CalcDialog_calcHeaderColor)
            val headerElevationColor = getColor(ta, R.styleable.CalcDialog_calcHeaderElevationColor)
            val separatorColor = getColor(ta, R.styleable.CalcDialog_calcDividerColor)
            val numberBtnColor = getColor(ta, R.styleable.CalcDialog_calcDigitBtnColor)
            val operationBtnColor = getColor(ta, R.styleable.CalcDialog_calcOperationBtnColor)
            ta.recycle()

            // Header
            calc_view_header_background.setBackgroundColor(headerColor)
            calc_view_header_elevation.setBackgroundColor(headerElevationColor)
            calc_view_header_elevation.visibility = View.GONE


            // Erase button
            calc_btn_erase.setOnEraseListener(object : EraseListener {
                override fun onErase() {
                    presenter!!.onErasedOnce()
                }

                override fun onEraseAll() {
                    presenter!!.onErasedAll()
                }
            })

            // Digit buttons
            for (i in 0..9) {
                val digitBtn = contentView.findViewById<TextView>(settings!!.numpadLayout.buttonIds[i])
                digitBtn.text = btnTexts[i]
                digitBtn.setOnClickListener { presenter!!.onDigitBtnClicked(i) }
            }
            calc_view_number_bg.setBackgroundColor(numberBtnColor)

            // Operator buttons
            calc_btn_add.text = btnTexts[TEXT_INDEX_ADD]
            calc_btn_sub.text = btnTexts[TEXT_INDEX_SUB]
            calc_btn_mul.text = btnTexts[TEXT_INDEX_MUL]
            calc_btn_div.text = btnTexts[TEXT_INDEX_DIV]
            calc_btn_add.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.ADD) }
            calc_btn_sub.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.SUBTRACT) }
            calc_btn_mul.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.MULTIPLY) }
            calc_btn_div.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.DIVIDE) }
            calc_view_op_bg.setBackgroundColor(operationBtnColor)

            // Sign button: +/-
            calc_btn_sign.text = btnTexts[TEXT_INDEX_SIGN]
            calc_btn_sign.setOnClickListener { presenter!!.onSignBtnClicked() }

            // Decimal separator button
            calc_btn_decimal.text = btnTexts[TEXT_INDEX_DEC_SEP]
            calc_btn_decimal.setOnClickListener { presenter!!.onDecimalSepBtnClicked() }

            // Equal button
            calc_btn_equal.text = btnTexts[TEXT_INDEX_EQUAL]
            calc_btn_equal.setOnClickListener { presenter!!.onEqualBtnClicked() }

            // Answer button
            calc_btn_answer.setOnClickListener { presenter!!.onAnswerBtnClicked() }

            // Divider
            calc_view_footer_divider.setBackgroundColor(separatorColor)

            // Dialog buttons
            calc_btn_clear.setOnClickListener { presenter!!.onClearBtnClicked() }

            calc_btn_cancel.setOnClickListener { presenter!!.onCancelBtnClicked() }

            calc_btn_ok.setOnClickListener { presenter!!.onOkBtnClicked() }

            // Set up dialog
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setOnShowListener { // Get maximum dialog dimensions
                val fgPadding = Rect()
                dialog.window!!.decorView.background.getPadding(fgPadding)
                val metrics =
                    Resources.getSystem().displayMetrics
                var height = metrics.heightPixels - fgPadding.top - fgPadding.bottom
                var width = metrics.widthPixels - fgPadding.top - fgPadding.bottom

                // Set dialog's dimensions
                if (width > maxDialogWidth) width = maxDialogWidth
                if (height > maxDialogHeight) height = maxDialogHeight
                dialog.window!!.setLayout(width, height)

                // Set dialog's content
                contentView.layoutParams = ViewGroup.LayoutParams(width, height)
                dialog.setContentView(contentView)

                // Presenter
                presenter = CalcPresenter()
                presenter!!.attach(this@CalcDialog, state)
            }
            if (state != null) {
                settings = state.getParcelable("settings")
            }
        }
        return dialog
    }

    private fun getColor(ta: TypedArray, index: Int): Int {
        val resId = ta.getResourceId(index, 0)
        return if (resId == 0) {
            // Raw color value e.g.: #FF000000
            ta.getColor(index, 0)
        } else {
            // Color reference pointing to color state list or raw color.
            AppCompatResources.getColorStateList(
                requireContext(),
                resId
            ).defaultColor
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (presenter != null) {
            // On config change, presenter is detached before this is called
            presenter!!.onDismissed()
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        if (presenter != null) {
            presenter!!.writeStateToBundle(state)
        }
        state.putParcelable("settings", settings)
    }

    override fun onDetach() {
        super.onDetach()
        if (presenter != null) {
            presenter!!.detach()
        }
        presenter = null
        fragcontext = null
    }// Interface callback is not implemented in activity// Caller was an activity// Interface callback is not implemented in fragment

    // Interface callback is not implemented in fragment
    private val callback: CalcDialogCallback?
        get() {
            var cb: CalcDialogCallback? = null
            when {
                parentFragment != null -> {
                    try {
                        cb = parentFragment as CalcDialogCallback?
                    } catch (e: Exception) {
                        // Interface callback is not implemented in fragment
                    }
                }
                targetFragment != null -> {
                    try {
                        cb = targetFragment as CalcDialogCallback?
                    } catch (e: Exception) {
                        // Interface callback is not implemented in fragment
                    }
                }
                else -> {
                    // Caller was an activity
                    try {
                        cb = requireActivity() as CalcDialogCallback
                    } catch (e: Exception) {
                        // Interface callback is not implemented in activity
                    }
                }
            }
            return cb
        }

    ////////// VIEW METHODS //////////
    fun exit() {
        dismissAllowingStateLoss()
    }

    fun sendValueResult(value: BigDecimal?) {
        callback?.onValueEntered(settings!!.requestCode, value)
    }

    fun setExpressionVisible(visible: Boolean) {
        contentView.apply {
            calc_hsv_expression.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    fun setAnswerBtnVisible(visible: Boolean) {
        contentView.apply {
            calc_btn_answer.visibility = if (visible) View.VISIBLE else View.INVISIBLE
            calc_btn_equal.visibility = if (visible) View.INVISIBLE else View.VISIBLE
        }
    }

    fun setSignBtnVisible(visible: Boolean) {
        contentView.apply {
            calc_btn_sign.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }
    }

    fun setDecimalSepBtnEnabled(enabled: Boolean) {
        contentView.apply {
            calc_btn_decimal.isEnabled = enabled
        }
    }

    fun updateExpression(text: String) {
        contentView.apply {
            calc_txv_expression.text = text
            // Scroll to the end.
            calc_hsv_expression.post { calc_hsv_expression.fullScroll(View.FOCUS_RIGHT) }
        }

    }

    fun updateCurrentValue(text: String?) {
        contentView.apply {
            calc_txv_value.text = text
        }
    }

    fun showErrorText(error: Int) {
        contentView.apply {
            calc_txv_value.text = errorMessages?.get(error) ?: ""
        }
    }

    fun showAnswerText() {
        contentView.apply {
            calc_txv_value.setText(R.string.calc_answer)
        }
    }

    interface CalcDialogCallback {
        /**
         * Called when the dialog's OK button is clicked.
         * @param value       value entered. May be null if no value was entered, in this case,
         * it should be interpreted as zero or absent value.
         * @param requestCode dialog request code from [CalcSettings.getRequestCode].
         */
        fun onValueEntered(requestCode: Int, value: BigDecimal?)
    }

    companion object {
        // Indexes of text elements in R.array.calc_dialog_btn_texts
        private const val TEXT_INDEX_ADD = 10
        private const val TEXT_INDEX_SUB = 11
        private const val TEXT_INDEX_MUL = 12
        private const val TEXT_INDEX_DIV = 13
        private const val TEXT_INDEX_SIGN = 14
        private const val TEXT_INDEX_DEC_SEP = 15
        private const val TEXT_INDEX_EQUAL = 16
    }
}
