package com.tomtruyen.budgettracker.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun toFormatString(date: Date) : String {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.US)

        return formatter.format(date)
    }

    fun toCurrencyString(value: Double) : String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

        return numberFormat.format(value)
    }
}