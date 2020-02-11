package com.nencer.nencerwallet.ext
import java.math.BigDecimal
import java.text.DecimalFormat

fun balance(value: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(value)
}

fun balance(value: BigDecimal): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(value)
}

fun balance(value: Double): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(value)
}