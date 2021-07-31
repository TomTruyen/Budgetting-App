package com.tomtruyen.budgettracker.models.settings

import java.util.*

class Settings(var currencyLocale: Locale, var dateLocale: Locale, var monthlyBudget: Double) {
    companion object {
        fun default(): Settings {
            return Settings(Locale.US, Locale.US, 0.0)
        }
    }
}