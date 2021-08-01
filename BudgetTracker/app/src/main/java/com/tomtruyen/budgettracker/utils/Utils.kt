package com.tomtruyen.budgettracker.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utils {
    fun toFormatString(date: Date): String {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.US)

        return formatter.format(date)
    }

    fun toCurrencyString(value: Double, locale: Locale = Locale.US): String {
        val numberFormat = NumberFormat.getCurrencyInstance(locale)

        return numberFormat.format(value)
    }

    fun toCurrencyDisplayNames(locales: List<Locale>): List<String> {
        val displayNames = ArrayList<String>()

        locales.forEach {
            val currency = NumberFormat.getCurrencyInstance(it).currency

            if (currency != null) {
                displayNames.add("${currency.displayName} (${currency.symbol})")
            }
        }

        return displayNames
    }

    fun densityToPixels(dp: Int, scale: Float): Int {
        return (dp * scale + 0.5f).toInt()
    }

    fun shouldShowAd() : Boolean {
        val random = Random()

        val generated = random.nextInt(100)

        // 25% chance of showing ad
        return generated < 25
    }
}