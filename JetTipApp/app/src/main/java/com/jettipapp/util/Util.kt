package com.jettipapp.util

fun calculateTipPercentage(sliderPosition: Float): Int {
    return (sliderPosition * 100).toInt()
}

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) {
        totalBill * tipPercentage / 100
    } else 0.0
}

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercentage: Int): Double {
    return (totalBill + calculateTotalTip(totalBill, tipPercentage)) / splitBy
}