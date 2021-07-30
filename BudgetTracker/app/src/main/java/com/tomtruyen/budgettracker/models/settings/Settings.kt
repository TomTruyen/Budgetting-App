package com.tomtruyen.budgettracker.models.settings

import java.util.*

class Settings(var currencyLocale: Locale, var dateLocale: Locale) {
    companion object {
        fun default(): Settings {
            return Settings(Locale.US, Locale.US)
        }
    }
}