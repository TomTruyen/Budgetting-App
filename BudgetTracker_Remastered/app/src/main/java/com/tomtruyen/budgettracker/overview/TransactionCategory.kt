package com.tomtruyen.budgettracker.overview

import com.tomtruyen.budgettracker.R

enum class TransactionCategory(val title: String, val icon: Int) {
    HOME("Home & Utilities", R.drawable.ic_home),
    TRAVEL("Travel", R.drawable.ic_travel),
    FITNESS("Fitness & Health", R.drawable.ic_fitness),
    FOOD("Food & Drinks", R.drawable.ic_food),
    INVESTMENT("Investments", R.drawable.ic_investment),
    OTHERS("Others", R.drawable.ic_other)
}