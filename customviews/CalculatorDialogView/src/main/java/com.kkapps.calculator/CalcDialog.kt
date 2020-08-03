package com.kkapps.calculator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.content.res.AppCompatResources
import com.kkapps.calculator.CalcEraseButton.EraseListener
import kotlinx.android.synthetic.main.dialog_calc.*
import java.math.BigDecimal

/**
 * Dialog with calculator for entering and calculating a number.
 * All settings must be set before showing the dialog or unexpected behavior will occur.
 */
class CalcDialog : AppCompatDialogFragment() {
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
        val inflater = LayoutInflater.from(context)
        val view =
            inflater.inflate(R.layout.dialog_calc, null)

        // Get attributes
        val ta = fragcontext!!.obtainStyledAttributes(R.styleable.CalcDialog)
        val btnTexts = ta.getTextArray(R.styleable.CalcDialog_calcButtonTexts)
        errorMessages = ta.getTextArray(R.styleable.CalcDialog_calcErrors)
        val maxDialogWidth = ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxWidth, -1)
        val maxDialogHeight = ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxHeight, -1)
        val headerColor = getColor(ta, R.styleable.CalcDialog_calcHeaderColor)
        val headerElevationColor = getColor(ta, R.styleable.CalcDialog_calcHeaderElevationColor)
        val separatorColor = getColor(ta, R.styleable.CalcDialog_calcDividerColor)
        val numberBtnColor = getColor(ta, R.styleable.CalcDialog_calcDigitBtnColor)
        val operationBtnColor = getColor(ta, R.styleable.CalcDialog_calcOperationBtnColor)
        ta.recycle()

        // Header
        val headerBgView =
            view.findViewById<View>(R.id.calc_view_header_background)
        val headerElevationBgView =
            view.findViewById<View>(R.id.calc_view_header_elevation)
        headerBgView.setBackgroundColor(headerColor)
        headerElevationBgView.setBackgroundColor(headerElevationColor)
        headerElevationBgView.visibility = View.GONE


        // Erase button
        val eraseBtn: CalcEraseButton = view.findViewById(R.id.calc_btn_erase)
        eraseBtn.setOnEraseListener(object : EraseListener {
            override fun onErase() {
                presenter!!.onErasedOnce()
            }

            override fun onEraseAll() {
                presenter!!.onErasedAll()
            }
        })

        // Digit buttons
        for (i in 0..9) {
            val digitBtn = view.findViewById<TextView>(settings!!.numpadLayout.buttonIds[i])
            digitBtn.text = btnTexts[i]
            digitBtn.setOnClickListener { presenter!!.onDigitBtnClicked(i) }
        }
        val numberBtnBgView = view.findViewById<View>(R.id.calc_view_number_bg)
        numberBtnBgView.setBackgroundColor(numberBtnColor)

        // Operator buttons
        val addBtn = view.findViewById<TextView>(R.id.calc_btn_add)
        val subBtn = view.findViewById<TextView>(R.id.calc_btn_sub)
        val mulBtn = view.findViewById<TextView>(R.id.calc_btn_mul)
        val divBtn = view.findViewById<TextView>(R.id.calc_btn_div)
        addBtn.text = btnTexts[TEXT_INDEX_ADD]
        subBtn.text = btnTexts[TEXT_INDEX_SUB]
        mulBtn.text = btnTexts[TEXT_INDEX_MUL]
        divBtn.text = btnTexts[TEXT_INDEX_DIV]
        addBtn.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.ADD) }
        subBtn.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.SUBTRACT) }
        mulBtn.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.MULTIPLY) }
        divBtn.setOnClickListener { presenter!!.onOperatorBtnClicked(Expression.Operator.DIVIDE) }
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
        val footerDividerView = view.findViewById<View>(R.id.calc_view_footer_divider)
        footerDividerView.setBackgroundColor(separatorColor)

        // Dialog buttons
        val clearBtn = view.findViewById<Button>(R.id.calc_btn_clear)
        clearBtn.setOnClickListener { presenter!!.onClearBtnClicked() }

        val cancelBtn = view.findViewById<Button>(R.id.calc_btn_cancel)
        cancelBtn.setOnClickListener { presenter!!.onCancelBtnClicked() }

        val okBtn = view.findViewById<Button>(R.id.calc_btn_ok)
        okBtn.setOnClickListener { presenter!!.onOkBtnClicked() }

        // Set up dialog
        val dialog = Dialog(context!!)
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
            view.layoutParams = ViewGroup.LayoutParams(width, height)
            dialog.setContentView(view)

            // Presenter
            presenter = CalcPresenter()
            presenter!!.attach(this@CalcDialog, state)
        }
        if (state != null) {
            settings = state.getParcelable("settings")
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
                context!!,
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
        val cb = callback
        cb?.onValueEntered(settings!!.requestCode, value)
    }

    fun setExpressionVisible(visible: Boolean) {
        calc_hsv_expression.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setAnswerBtnVisible(visible: Boolean) {
        calc_btn_answer.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        calc_btn_equal.visibility = if (visible) View.INVISIBLE else View.VISIBLE
    }

    fun setSignBtnVisible(visible: Boolean) {
        calc_btn_sign.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun setDecimalSepBtnEnabled(enabled: Boolean) {
        calc_btn_decimal.isEnabled = enabled
    }

    fun updateExpression(text: String) {
        calc_txv_expression.text = text

        // Scroll to the end.
        calc_hsv_expression.post { calc_hsv_expression.fullScroll(View.FOCUS_RIGHT) }
    }

    fun updateCurrentValue(text: String?) {
        calc_txv_value.text = text
    }

    fun showErrorText(error: Int) {
        calc_txv_value.text = errorMessages?.get(error) ?: ""
    }

    fun showAnswerText() {
        calc_txv_value.setText(R.string.calc_answer)
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
