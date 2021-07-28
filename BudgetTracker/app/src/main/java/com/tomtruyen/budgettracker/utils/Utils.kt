package com.tomtruyen.budgettracker.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    fun toFormatString(date: Date) : String {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.US)

        return formatter.format(date)
    }
}