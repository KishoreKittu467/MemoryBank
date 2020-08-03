package com.kkapps.calculator

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat

//import java.text.DecimalFormatSymbols;
/**
 * Settings for the calculator dialog.
 */
class CalcSettings : Parcelable {
    private val tag: String = "CalcSettings"
    var requestCode = 0

    // Appearance settings
    var numberFormat: NumberFormat = NumberFormat.getInstance()
    var maxIntDigits = 10
    var numpadLayout = CalcNumpadLayout.CALCULATOR
    var isExpressionShown = false
    var isZeroShownWhenNoValue = true
    var isAnswerBtnShown = false
    var isSignBtnShown = true
    var isExpressionEditable = false
    var isShouldEvaluateOnOperation = false

    // Behavior settings
    var initialValue: BigDecimal? = null
    var minValue: BigDecimal? = BigDecimal("-1E10")
    var maxValue: BigDecimal? = BigDecimal("1E10")
    var isOrderOfOperationsApplied = true

    internal constructor() {
        numberFormat.maximumIntegerDigits = Int.MAX_VALUE
        numberFormat.maximumFractionDigits = 8
    }

    fun validate() {
        require(!(minValue != null && maxValue != null && minValue!! >= maxValue)) { "Minimum value must be less than maximum value." }
    }

    /**
     * Set the request code by which the dialog is identified
     * @param requestCode A request code.
     * @return The settings
     */
    fun setRequestCode(requestCode: Int): CalcSettings {
        this.requestCode = requestCode
        return this
    }

    /**
     * Set the number format to use for formatting the currently displayed value and the
     * values in the expression (if shown). This can be used to set a prefix and a suffix,
     * changing the grouping settings, the minimum and maximum integer and fraction digits,
     * the decimal separator, the rounding mode, and probably more.
     * By default, the locale's default decimal format is used.
     * @param format A number format.
     * @return The settings
     * @see NumberFormat
     */
    fun setNumberFormat(format: NumberFormat): CalcSettings {
        require(format.roundingMode != RoundingMode.UNNECESSARY) { "Cannot use RoundingMode.UNNECESSARY as a rounding mode." }
        numberFormat = format

        // The max int setting on number format is used to set the maximum int digits that can be entered.
        // However, it is possible that the user evaluates expressions resulting in bigger numbers.
        // If not changed, this would show those values as "0,000" instead of "10,000" for example.
        maxIntDigits = numberFormat.maximumIntegerDigits
        numberFormat.maximumIntegerDigits = Int.MAX_VALUE
        return this
    }

    /**
     * Set the layout of the calculator's numpad, either with 123 on the top row or 789.
     * Default layout is [CalcNumpadLayout.CALCULATOR], with 789 on the top row.
     * @param layout Numpad layout to use
     * @return The settings
     * @see CalcNumpadLayout
     */
    fun setNumpadLayout(layout: CalcNumpadLayout): CalcSettings {
        numpadLayout = layout
        return this
    }

    /**
     * Set whether to show the expression above the value when the user is typing it.
     * By default, the expression is not shown.
     * @param shown Whether to show it or not.
     * @return The settings
     */
    fun setExpressionShown(shown: Boolean): CalcSettings {
        isExpressionShown = shown
        return this
    }

    /**
     * Set whether to the expression can be edited by erasing further than the current value.
     * By default the expression is not editable.
     * @param editable Whether to show it or not.
     * @return The settings
     */
    fun setExpressionEditable(editable: Boolean): CalcSettings {
        isExpressionEditable = editable
        return this
    }

    /**
     * Set whether zero should be displayed when no value has been entered or just display nothing.
     * This happens when initial value is null, when an error is dismissed, or when an operator
     * is clicked and [.shouldEvaluateOnOperation] is set to true.
     * @param shown Whether to show it or not.
     * @return The settings
     */
    fun setZeroShownWhenNoValue(shown: Boolean): CalcSettings {
        isZeroShownWhenNoValue = shown
        return this
    }

    /**
     * Set whether to show the answer button when an operation button is clicked or not.
     * This button allows the user to enter the value that was previously calculated.
     * By default, the answer button is not shown.
     * @param shown Whether to show it or not.
     * @return The settings
     */
    fun setAnswerBtnShown(shown: Boolean): CalcSettings {
        isAnswerBtnShown = shown
        return this
    }

    /**
     * Set whether the sign button should be shown. By default it is shown.
     * @param shown Whether to show it or not.
     * @return The settings
     */
    fun setSignBtnShown(shown: Boolean): CalcSettings {
        isSignBtnShown = shown
        return this
    }

    /**
     * Set whether to evaluate the expression when an operation button is pressed (+, -, * and /).
     * If not, the display will show zero or no value if [.isZeroShownWhenNoValue] is true.
     * By default, the expression is evaluated.
     * @param shouldClear Whether to evaluate it or not.
     * @return The settings
     */
    fun setShouldEvaluateOnOperation(shouldClear: Boolean): CalcSettings {
        isShouldEvaluateOnOperation = shouldClear
        return this
    }

    /**
     * Set initial value to show. It must be within minimum and maximum values.
     * If null and [.isZeroShownWhenNoValue] is set to false, no value will be shown.
     * By default, initial value is null, which results in a 0 for the calculator.
     * @param value Initial value to display. Use null for no value.
     * @return The settings
     */
    fun setInitialValue(value: BigDecimal?): CalcSettings {
        initialValue = value
        return this
    }

    /**
     * Set minimum value that can be entered.
     * If the minimum value is exceeded, an "Out of bounds" error will be shown when user clicks OK.
     * Default minimum is -10,000,000,000 (-1e+10).
     * @param minValue Minimum value, use null for no minimum.
     * @return The settings
     */
    fun setMinValue(minValue: BigDecimal?): CalcSettings {
        this.minValue = minValue
        return this
    }

    /**
     * Set maximum value that can be entered.
     * If the maximum value is exceeded, an "Out of bounds" error will be shown when user clicks OK.
     * Default maximum is 10,000,000,000 (1e+10).
     * @param maxValue Maximum value, use null for no maximum.
     * @return The settings
     */
    fun setMaxValue(maxValue: BigDecimal?): CalcSettings {
        this.maxValue = maxValue
        return this
    }

    /**
     * Set whether to apply the operation priority on the entered expression, i.e. evaluating
     * products and quotients before, from left to right.
     * If not, the operations are evaluated in the same order as they are entered.
     * @param isApplied Whether to apply operation priority or not.
     * @return The settings
     */
    fun setOrderOfOperationsApplied(isApplied: Boolean): CalcSettings {
        isOrderOfOperationsApplied = isApplied
        return this
    }

    ////////// PARCELABLE //////////
    private constructor(`in`: Parcel) {
        val bundle = `in`.readBundle(javaClass.classLoader)
        if (bundle != null) {
            numberFormat = getNumberFormatFromBundle(bundle)!!
            requestCode = bundle.getInt("requestCode")
            numpadLayout = (bundle.getSerializable("numpadLayout") as CalcNumpadLayout?)!!
            isExpressionShown = bundle.getBoolean("isExpressionShown")
            isZeroShownWhenNoValue = bundle.getBoolean("isZeroShownWhenNoValue")
            isAnswerBtnShown = bundle.getBoolean("isAnswerBtnShown")
            isSignBtnShown = bundle.getBoolean("isSignBtnShown")
            isShouldEvaluateOnOperation = bundle.getBoolean("shouldEvaluateOnOperation")
            if (bundle.containsKey("initialValue")) {
                initialValue = bundle.getSerializable("initialValue") as BigDecimal?
            }
            if (bundle.containsKey("minValue")) {
                minValue = bundle.getSerializable("minValue") as BigDecimal?
            }
            if (bundle.containsKey("maxValue")) {
                maxValue = bundle.getSerializable("maxValue") as BigDecimal?
            }
            isOrderOfOperationsApplied = bundle.getBoolean("isOrderOfOperationsApplied")
        }
    }

    private fun putNumberFormatInBundle(bundle: Bundle) {
        bundle.putSerializable("nbFormat", numberFormat)
        if (numberFormat is DecimalFormat) {
            bundle.putSerializable(
                "nbfmtPattern",
                (numberFormat as DecimalFormat).toPattern()
            )
        }
    }

    private fun getNumberFormatFromBundle(bundle: Bundle): NumberFormat? {
        var nbFmt: NumberFormat? = null
        try {
            nbFmt = bundle.getSerializable("nbFormat") as NumberFormat?
        } catch (npe: NullPointerException) {
            // Very rarely and on API >= 28, Bundle will fail to get serialized NumberFormat.
            // This issue is related to: https://stackoverflow.com/a/54155356/5288316.
            // Luckily, NumberFormat is most often a DecimalFormat, which can be saved
            // using a pattern. Note that the naming of the key is important here, it must
            // not start with "nbFormat" for some reason!
            if (bundle.containsKey("nbfmtPattern")) {
                nbFmt = DecimalFormat(bundle.getString("nbfmtPattern", ""))
            } else {
                Log.e(
                    tag,
                    "Failed to deserialize NumberFormat."
                )
            }
            // Otherwise number format is lost for good, keep default.
        }
        if (nbFmt == null) {
            nbFmt = NumberFormat.getInstance()
        }
        return nbFmt
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(requestCode)
        parcel.writeInt(maxIntDigits)
        parcel.writeByte(if (isExpressionShown) 1 else 0)
        parcel.writeByte(if (isZeroShownWhenNoValue) 1 else 0)
        parcel.writeByte(if (isAnswerBtnShown) 1 else 0)
        parcel.writeByte(if (isSignBtnShown) 1 else 0)
        parcel.writeByte(if (isExpressionEditable) 1 else 0)
        parcel.writeByte(if (isShouldEvaluateOnOperation) 1 else 0)
        parcel.writeByte(if (isOrderOfOperationsApplied) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalcSettings> {
        override fun createFromParcel(parcel: Parcel): CalcSettings {
            return CalcSettings(parcel)
        }

        override fun newArray(size: Int): Array<CalcSettings?> {
            return arrayOfNulls(size)
        }
    }
}
