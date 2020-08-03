package com.kkapps.calculator

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

internal class Expression() : Parcelable {
    val numbers: MutableList<BigDecimal> =
        ArrayList()
    val operators = mutableListOf<Operator>()

    fun clear() {
        numbers.clear()
        operators.clear()
    }

    val isNotEmpty: Boolean
        get() = numbers.size != 0

    constructor(parcel: Parcel) : this() {
    }

    /**
     * Evaluate the expression and return the result.
     * @param priority     Whether to apply operation priority or not.
     * @param scale        Scale used for division.
     * @param roundingMode Rounding mode used for division.
     * @return The result.
     * @throws ArithmeticException if a division by zero occurred.
     */
    fun evaluate(
        priority: Boolean,
        scale: Int,
        roundingMode: RoundingMode?
    ): BigDecimal {
        check(numbers.size == operators.size + 1) { "Numbers and operators aren't balanced." }
        if (numbers.size == 1) return numbers[0]
        val nbs: MutableList<BigDecimal> =
            ArrayList(numbers)
        val ops: MutableList<Operator> = operators
        if (priority) {
            // Evaluate products and quotients
            var i = 0
            while (i < ops.size) {
                when (ops[i]) {
                    Operator.MULTIPLY -> {
                        ops.removeAt(i)
                        val n1 = nbs[i]
                        val n2 = nbs.removeAt(i + 1)
                        nbs[i] = n1.multiply(n2)
                    }
                    Operator.DIVIDE -> {
                        ops.removeAt(i)
                        val n1 = nbs[i]
                        val n2 = nbs.removeAt(i + 1)
                        nbs[i] = n1.divide(n2, scale, roundingMode)
                    }
                    else -> {
                        i++
                    }
                }
            }
        }

        // Evaluate the rest
        while (ops.isNotEmpty()) {
            val op: Operator = ops.removeAt(0)
            val n1 = nbs[0]
            val n2 = nbs.removeAt(1)
            when (op) {
                Operator.ADD -> { nbs[0] = n1.add(n2) }
                Operator.SUBTRACT -> { nbs[0] = n1.subtract(n2) }
                Operator.MULTIPLY -> { nbs[0] = n1.multiply(n2) }
                else -> { nbs[0] = n1.divide(n2, scale, roundingMode) }
            }
        }
        return nbs.removeAt(0).stripTrailingZeros()
    }

    /**
     * Format the expression to a string.
     * @param nbFormat The format to use for formatting numbers.
     * @return The expression string.
     */
    fun format(nbFormat: NumberFormat): String {
        val sb = StringBuilder()
        for (i in numbers.indices) {
            sb.append(nbFormat.format(numbers[i]))
            sb.append(' ')
            if (i < operators.size) {
                sb.append(operators[i].symbol)
            }
            sb.append(' ')
        }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }

    override fun toString(): String {
        return format(NumberFormat.getInstance())
    }

    internal enum class Operator(var symbol: Char) {
        ADD('+'), SUBTRACT('−'), MULTIPLY('×'), DIVIDE('÷');
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expression> {
        override fun createFromParcel(parcel: Parcel): Expression {
            return Expression(parcel)
        }

        override fun newArray(size: Int): Array<Expression?> {
            return arrayOfNulls(size)
        }
    }
}
