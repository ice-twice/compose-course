package com.jetweatherforecast.ui.screens.main

class DecimalFormatter {
    companion object {
        fun formatDecimal(item: Float): String = "%.0f".format(item)
    }
}
