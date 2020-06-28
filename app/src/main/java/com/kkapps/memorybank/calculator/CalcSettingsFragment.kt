package com.kkapps.memorybank.calculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.kkapps.memorybank.R
import kotlinx.android.synthetic.main.fragment_calc_settings.*
import java.math.BigDecimal
import java.text.NumberFormat

class CalcSettingsFragment : Fragment(), CalcDialog.CalcDialogCallback {


    private var value: BigDecimal? = null
    private var nbFmt = NumberFormat.getInstance()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calc_settings, container, false)
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        val fm = childFragmentManager
        val calcDialog = fm.findFragmentByTag(DIALOG_TAG) as? CalcDialog ?: CalcDialog()

        // Dark theme
        dark_theme_chk.setOnCheckedChangeListener { _, _ ->
            updateTheme()
        }

        // Min value
        val minValChk: CheckBox = min_value_chk
        val minValueInput: EditText = min_value_input
        minValChk.setOnCheckedChangeListener { _, isChecked ->
            minValueInput.isEnabled = isChecked
        }
        minValueInput.isEnabled = minValChk.isChecked

        // Max value
        val maxValChk = max_value_chk
        val maxValInput: EditText = max_value_input
        maxValChk.setOnCheckedChangeListener { _, isChecked ->
            maxValInput.isEnabled = isChecked
        }
        maxValInput.isEnabled = maxValChk.isChecked

        // Numpad layout
        var numpadLayout = CalcNumpadLayout.CALCULATOR
        setupDropdown(numpad_layout_dropdown, R.array.numpad_layout_dropdown_values) {
            numpadLayout = when (it) {
                0 -> CalcNumpadLayout.CALCULATOR
                1 -> CalcNumpadLayout.PHONE
                else -> error("Invalid numpad layout dropdown index")
            }
        }

        // Number format
        setupDropdown(nbfmt_dropdown, R.array.nbfmt_dropdown_values) {
            nbFmt = when (it) {
                0 -> NumberFormat.getInstance()
                1 -> NumberFormat.getCurrencyInstance()
                else -> error("Invalid number format dropdown index")
            }
            updateNumberFormat()
        }

        // Max integer digits
        val maxIntDigitsChk: CheckBox = max_int_digits_chk
        val maxIntDigitsInput: EditText = max_int_digits_input
        maxIntDigitsChk.setOnCheckedChangeListener { _, isChecked ->
            maxIntDigitsInput.isEnabled = isChecked
            updateNumberFormat()
        }
        maxIntDigitsInput.doAfterTextChanged {
            updateNumberFormat()
        }
        maxIntDigitsInput.isEnabled = maxIntDigitsChk.isChecked

        // Max fractional digits
        val maxFracDigitsChk = max_frac_digits_chk
        val maxFracDigitsInput: EditText = max_frac_digits_input
        maxFracDigitsChk.setOnCheckedChangeListener { _, isChecked ->
            maxFracDigitsInput.isEnabled = isChecked
            updateNumberFormat()
        }
        maxFracDigitsInput.doAfterTextChanged {
            updateNumberFormat()
        }
        maxFracDigitsInput.isEnabled = maxFracDigitsChk.isChecked

        // Open dialog click listener
        val openDialogClickListener = View.OnClickListener {
            if (fm.findFragmentByTag(DIALOG_TAG) != null) {
                // Dialog is already shown.
                return@OnClickListener
            }

            // Get settings from views
            val minValue = if (minValChk.isChecked && minValueInput.length() > 0) {
                BigDecimal(minValueInput.text.toString())
            } else {
                null
            }
            var maxValue = if (maxValChk.isChecked && maxValInput.length() > 0) {
                BigDecimal(maxValInput.text.toString())
            } else {
                null
            }
            if (minValue != null && maxValue != null && minValue > maxValue) {
                // Min can't be greater than max, disable max value.
                maxValChk.isChecked = false
                maxValue = null
            }

            // Update dialog settings
            calcDialog.settings.let {
                it.initialValue = value
                it.isExpressionShown = show_expr_chk.isChecked
                it.isExpressionEditable = expr_editable_chk.isChecked
                it.isAnswerBtnShown = show_answer_btn_chk.isChecked
                it.isSignBtnShown = show_sign_btn_chk.isChecked
                it.isOrderOfOperationsApplied = order_operation_chk.isChecked
                it.isShouldEvaluateOnOperation = eval_operation_chk.isChecked
                it.isZeroShownWhenNoValue = show_zero_chk.isChecked
                it.minValue = minValue
                it.maxValue = maxValue
                it.numpadLayout = numpadLayout
                it.numberFormat = nbFmt
            }

            // Show the dialog
            calcDialog.show(fm, DIALOG_TAG)
        }
        selected_foreground_view.setOnClickListener(openDialogClickListener)
        fab_calc.setOnClickListener(openDialogClickListener)

        if (state == null) {
            // Set initial state
            minValueInput.setText(BigDecimal("-1E10").toPlainString())
            maxValInput.setText(BigDecimal("1E10").toPlainString())
            maxIntDigitsInput.setText(10.toString())
            maxFracDigitsInput.setText(8.toString())
            numpad_layout_dropdown.setSelection(0)
            nbfmt_dropdown.setSelection(0)
            updateNumberFormat()
            updateTheme()

        } else {
            // Restore state
            value = state.getSerializable("value") as BigDecimal?
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        if (value != null) {
            state.putSerializable("value", value)
        }
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        if (requestCode == DIALOG_REQUEST_CODE) {
            this.value = value
            updateSelectedValueText()
        }
    }

    private fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(
                if (dark_theme_chk.isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
        )
    }

    private fun updateNumberFormat() {
        nbFmt.maximumIntegerDigits = max_int_digits_input.text.toString().toIntOrNull().takeIf {
            max_int_digits_chk.isChecked
        } ?: Integer.MAX_VALUE
        nbFmt.maximumFractionDigits = max_frac_digits_input.text.toString().toIntOrNull().takeIf {
            max_frac_digits_chk.isChecked
        } ?: Integer.MAX_VALUE
        updateSelectedValueText()
    }

    private fun updateSelectedValueText() {
        val valueTxv = selected_value_txv
        if (value == null) {
            valueTxv.setText(R.string.selection_value_none)
            valueTxv.alpha = 0.5f
        } else {
            valueTxv.text = nbFmt.format(value)
            valueTxv.alpha = 1.0f
        }
    }

    private inline fun setupDropdown(dropdown: AutoCompleteTextView, @ArrayRes items: Int,
                                     crossinline onItemSelected: (pos: Int) -> Unit = {}) {
        val context = requireContext()
        val adapter = DropdownAdapter(context, context.resources.getStringArray(items).toList())
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener { _, _, pos, _ ->
            dropdown.requestLayout()
            onItemSelected(pos)
        }
    }

    /**
     * Custom AutoCompleteTextView adapter to disable filtering since we want it to act like a spinner.
     */
    private class DropdownAdapter(context: Context, items: List<String> = mutableListOf()) :
            ArrayAdapter<String>(context, R.layout.item_calc_dropdown, items) {

        override fun getFilter() = object : Filter() {
            override fun performFiltering(constraint: CharSequence?) = null
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
        }
    }

    companion object {
        private const val DIALOG_REQUEST_CODE = 0
        private const val DIALOG_TAG = "calc_dialog"
    }
}
