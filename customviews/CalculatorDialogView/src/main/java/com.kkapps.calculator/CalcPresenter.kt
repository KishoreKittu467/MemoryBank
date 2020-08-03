package com.kkapps.calculator

import android.os.Bundle
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.max

internal class CalcPresenter {
    private var view: CalcDialog? = null
    private var settings: CalcSettings? = null
    private var nbFormat: NumberFormat? = null

    /** The typed expression.  */
    private var expression = Expression()

    /**
     * The current displayed value. Can be user input, evaluated result, answer from the answer
     * button, or can be null if there's no current value.
     */
    private var currentValue: BigDecimal? = null

    /** The last evaluated result, null for none.  */
    private var resultValue: BigDecimal? = null

    /**
     * The number of digits shown after the decimal separator in [.currentValue].
     * If -1, there's no fractional part. If 0, only the decimal separator is shown.
     * If more than 0, indicates the number of fraction digits.
     */
    private var currentValueScale = 0

    /** If there's an error, the error code.  */
    private var errorCode = 0

    /** Whether [.currentValue] is from the answer button.  */
    private var currentIsAnswer = false

    /** Whether [.currentValue] is the result from the equal button.  */
    private var currentIsResult = false

    /**
     * Whether user can edit [.currentValue] or not.
     * If not editable, a button press clears the current value,
     * with the exception of the sign button which will still negate the value.
     */
    private var canEditCurrentValue = false

    /**
     * Whether user can edit expression or not.
     * If not editable, a button press clears the expression.
     */
    private var canEditExpression = false
    fun attach(v: CalcDialog?, state: Bundle?) {
        view = v
        settings = view!!.settings
        nbFormat = settings!!.numberFormat
        if (state == null) {
            reset()
            currentValue = settings!!.initialValue
        } else {
            readStateFromBundle(state)
        }
        view!!.setExpressionVisible(settings!!.isExpressionShown)
        view!!.setDecimalSepBtnEnabled(nbFormat!!.maximumFractionDigits > 0)
        view!!.setAnswerBtnVisible(settings!!.isAnswerBtnShown && resultValue != null)
        view!!.setSignBtnVisible(settings!!.isSignBtnShown)
        updateCurrentValue()
        updateExpression()
    }

    fun detach() {
        view = null
        settings = null
    }

    fun writeStateToBundle(bundle: Bundle) {
        bundle.putParcelable("expression", expression)
        if (currentValue != null) {
            bundle.putSerializable("currentValue", currentValue)
        }
        if (resultValue != null) {
            bundle.putSerializable("resultValue", resultValue)
        }
        bundle.putInt("currentValueScale", currentValueScale)
        bundle.putInt("errorCode", errorCode)
        bundle.putBoolean("currentIsAnswer", currentIsAnswer)
        bundle.putBoolean("currentIsResult", currentIsResult)
        bundle.putBoolean("canEditCurrentValue", canEditCurrentValue)
        bundle.putBoolean("canEditExpression", canEditExpression)
    }

    private fun readStateFromBundle(bundle: Bundle) {
        val expr: Expression? = bundle.getParcelable("expression")
        if (expr != null) {
            expression = expr
        }
        if (bundle.containsKey("currentValue")) {
            currentValue = bundle.getSerializable("currentValue") as BigDecimal?
        }
        if (bundle.containsKey("resultValue")) {
            resultValue = bundle.getSerializable("resultValue") as BigDecimal?
        }
        currentValueScale = bundle.getInt("currentValueScale")
        errorCode = bundle.getInt("errorCode")
        currentIsAnswer = bundle.getBoolean("currentIsAnswer")
        currentIsResult = bundle.getBoolean("currentIsResult")
        canEditCurrentValue = bundle.getBoolean("canEditCurrentValue")
        canEditExpression = bundle.getBoolean("canEditExpression")
    }

    fun onErasedOnce() {
        clearExpressionIfNeeded()
        if (dismissError()) return
        currentIsAnswer = false
        currentIsResult = false
        view!!.setAnswerBtnVisible(false)
        if (!canEditCurrentValue) {
            currentValue = null
            canEditCurrentValue = true
        } else if (currentValue != null) {
            var valueStr = currentValueString
            valueStr = valueStr.substring(0, valueStr.length - 1)
            try {
                currentValue = BigDecimal(valueStr)
                if (currentValueScale >= 0) {
                    currentValueScale--
                }
            } catch (e: NumberFormatException) {
                // Happens if string is empty or "-".
                currentValue = null
                currentValueScale = -1
            }
        } else if (settings!!.isExpressionEditable && expression.isNotEmpty) {
            // No more digits to erase: pop last expression number and operator and make it current value
            currentValue = expression.numbers.removeAt(expression.numbers.size - 1)
            expression.operators.removeAt(expression.operators.size - 1)
            currentValueScale = currentValue?.scale() ?: -1
            if (currentValueScale == 0) currentValueScale = -1
            updateExpression()
        }
        updateCurrentValue()
    }

    fun onErasedAll() {
        onClearBtnClicked()
    }

    fun onDigitBtnClicked(digit: Int) {
        clearExpressionIfNeeded()
        dismissOldValue()
        val valueStr = currentValueString

        // Check if max digits has been exceeded
        val pointPos = valueStr.indexOf('.')
        val maxIntReached =
            pointPos == -1 && valueStr.length >= settings!!.maxIntDigits
        val maxFracReached =
            pointPos != -1 && valueStr.length - pointPos - 1 >= nbFormat!!.maximumFractionDigits
        if (maxIntReached || maxFracReached) {
            // Can't add a new digit, it's already at the maximum.
            return
        }
        if (pointPos != -1) {
            currentValueScale++
        }
        currentValue = BigDecimal(valueStr + digit)
        updateCurrentValue()
    }

    fun onOperatorBtnClicked(operator: Expression.Operator) {
        clearExpressionIfNeeded()
        if (dismissError()) return
        currentIsResult = false
        currentValueScale = -1
        if (!currentIsAnswer && !canEditCurrentValue && expression.operators.isNotEmpty()) {
            // Undo previous operator button click if the current value is the
            // result of the expression calculated on the last button click.
            expression.operators[expression.operators.size - 1] = operator
        } else {
            if (currentValue == null) {
                currentValue = BigDecimal.ZERO
            }
            expression.numbers.add(currentValue!!)
            calculate()
            expression.operators.add(operator)
            if (!settings!!.isShouldEvaluateOnOperation) {
                currentValue = null
            }
        }
        view!!.setAnswerBtnVisible(settings!!.isAnswerBtnShown && resultValue != null)
        updateCurrentValue()
        updateExpression()
    }

    fun onDecimalSepBtnClicked() {
        clearExpressionIfNeeded()
        dismissOldValue()
        if (currentValueScale == -1) {
            // Only insert a decimal point if there isn't one yet
            if (currentValue == null) {
                currentValue = BigDecimal.ZERO
            }
            currentValueScale = 0
            updateCurrentValue()
        }
    }

    fun onSignBtnClicked() {
        dismissError()
        currentIsAnswer = false
        view!!.setAnswerBtnVisible(false)
        if (!canEditCurrentValue && !currentIsResult && expression.isNotEmpty) {
            // If current value is result, it's not editable but still allow negation.
            // If current value is result and isn't editable, but expression is empty,
            // that means current value is initial value, so allow negation too.
            // Otherwise, clear value.
            currentValue = null
            canEditCurrentValue = true
            currentValueScale = -1
        }

        // Negate value if there's one and it's not zero.
        if (currentValue != null && currentValue!!.compareTo(BigDecimal.ZERO) != 0) {
            currentValue = currentValue!!.negate()
        }
        updateCurrentValue()
    }

    fun onEqualBtnClicked() {
        clearExpressionIfNeeded()
        if (dismissError()) return
        equal()
    }

    fun onAnswerBtnClicked() {
        currentValue = resultValue
        currentValueScale = -1
        currentIsAnswer = true
        canEditCurrentValue = false
        view!!.setAnswerBtnVisible(false)
        updateCurrentValue()
    }

    fun onClearBtnClicked() {
        clearExpressionIfNeeded()
        if (dismissError()) return
        reset()
        view!!.setAnswerBtnVisible(false)
        updateCurrentValue()
        updateExpression()
    }

    fun onCancelBtnClicked() {
        view!!.exit()
    }

    fun onOkBtnClicked() {
        clearExpressionIfNeeded()
        if (dismissError()) return
        equal()
        if (expression.numbers.size > 1) {
            // If the expression still has more than 1 number it means it was just calculated.
            // Don't dismiss already to let user see the result.
            return
        }
        if (resultValue != null) {
            // Check if value is out of bounds and if so, show an error.
            // Show special error messages if minimum or maximum is 0.
            if (settings!!.maxValue != null && resultValue!! > settings!!.maxValue) {
                if (settings!!.maxValue!!.compareTo(BigDecimal.ZERO) == 0) {
                    setError(ERROR_WRONG_SIGN_NEG)
                } else {
                    setError(ERROR_OUT_OF_BOUNDS)
                }
                return
            } else if (settings!!.minValue != null && resultValue!! < settings!!.minValue) {
                if (settings!!.minValue!!.compareTo(BigDecimal.ZERO) == 0) {
                    setError(ERROR_WRONG_SIGN_POS)
                } else {
                    setError(ERROR_OUT_OF_BOUNDS)
                }
                return
            }
        }
        if (errorCode == ERROR_NONE) {
            view!!.sendValueResult(resultValue)
            view!!.exit()
        }
    }

    fun onDismissed() {
        reset()
    }

    private fun clearExpressionIfNeeded() {
        if (!canEditExpression) {
            expression.clear()
            canEditExpression = true
            currentIsResult = false
            updateExpression()
        }
    }

    private fun dismissOldValue() {
        dismissError()
        currentIsAnswer = false
        view!!.setAnswerBtnVisible(false)
        if (!canEditCurrentValue) {
            currentValue = null
            canEditCurrentValue = true
            currentValueScale = -1
        }
    }

    /**
     * Reset all variables to their initial value. Doesn't update the display.
     */
    private fun reset() {
        expression.clear()
        currentValue = null
        resultValue = null
        currentValueScale = -1
        errorCode = ERROR_NONE
        currentIsAnswer = false
        currentIsResult = false
        canEditCurrentValue = false
        canEditExpression = true
        view!!.setAnswerBtnVisible(false)
    }

    private fun calculate() {
        currentValue = try {
            expression.evaluate(
                settings!!.isOrderOfOperationsApplied,
                settings!!.numberFormat.maximumFractionDigits, nbFormat!!.roundingMode
            )
        } catch (e: ArithmeticException) {
            // Division by zero occurred.
            setError(ERROR_DIV_ZERO)
            return
        }
        currentValueScale = -1
        currentIsAnswer = false
        canEditCurrentValue = false
    }

    private fun equal() {
        if (!currentIsAnswer && !canEditCurrentValue && expression.operators.isNotEmpty()) {
            // Remove unused last operator
            expression.operators.removeAt(expression.operators.size - 1)
        } else {
            if (currentValue == null) {
                currentValue = BigDecimal.ZERO
            }
            expression.numbers.add(currentValue!!)
        }
        calculate()
        if (errorCode == ERROR_NONE) {
            resultValue = currentValue
            currentIsResult = true
            currentValueScale = -1
            updateCurrentValue()
        }
        canEditExpression = false
        updateExpression()
    }

    private fun setError(error: Int) {
        errorCode = error

        // Reset all but not the expression.
        currentValue = null
        resultValue = null
        currentValueScale = -1
        currentIsAnswer = false
        canEditCurrentValue = false
        canEditExpression = false
        view!!.showErrorText(error)
    }

    private fun dismissError(): Boolean {
        if (errorCode != ERROR_NONE) {
            errorCode = ERROR_NONE
            updateCurrentValue()
            return true
        }
        return false
    }

    private fun updateCurrentValue() {
        if (currentIsAnswer) {
            view!!.showAnswerText()
            return
        }
        var value = currentValue
        if (value == null && settings!!.isZeroShownWhenNoValue) {
            value = BigDecimal.ZERO
        }
        var text: String? = null
        if (value != null) {
            if (currentValueScale > 0 && nbFormat!!.minimumFractionDigits < currentValueScale) {
                // Set a minimum number of fraction digits so that trailing zeroes are shown.
                val minFracBefore = nbFormat!!.minimumFractionDigits
                nbFormat!!.minimumFractionDigits = currentValueScale
                text = nbFormat!!.format(value)
                nbFormat!!.minimumFractionDigits = minFracBefore
            } else if (currentValueScale == 0 && nbFormat!!.minimumFractionDigits == 0 && nbFormat is DecimalFormat
            ) {
                // Append the decimal separator at the end of the number.
                val fmt = nbFormat as DecimalFormat
                val sep = fmt.decimalFormatSymbols.decimalSeparator
                if (value >= BigDecimal.ZERO) {
                    val suffixBefore = fmt.positiveSuffix
                    fmt.positiveSuffix = sep.toString() + suffixBefore
                    text = nbFormat?.format(value)
                    fmt.positiveSuffix = suffixBefore
                } else {
                    val suffixBefore = fmt.negativeSuffix
                    fmt.negativeSuffix = sep.toString() + suffixBefore
                    text = nbFormat?.format(value)
                    fmt.negativeSuffix = suffixBefore
                }
            } else {
                text = nbFormat!!.format(value)
            }
        }
        view!!.updateCurrentValue(text)
    }

    private fun updateExpression() {
        if (settings!!.isExpressionShown) {
            var text = expression.format(nbFormat!!)
            if (currentIsResult) {
                // If current value is the result from the equal button, append = to the expression.
                text += " ="
            }
            view!!.updateExpression(text)
        }
    }

    private val currentValueString: String
        get() {
            if (currentValue == null) return ""
            var str = currentValue!!.setScale(
                max(0, currentValueScale),
                RoundingMode.UNNECESSARY
            ).toPlainString()
            if (currentValueScale == 0) {
                str += '.'
            }
            return str
        }

    companion object {
        private const val ERROR_NONE = -1
        private const val ERROR_DIV_ZERO = 0
        private const val ERROR_OUT_OF_BOUNDS = 1
        private const val ERROR_WRONG_SIGN_POS = 2
        private const val ERROR_WRONG_SIGN_NEG = 3
    }
}
