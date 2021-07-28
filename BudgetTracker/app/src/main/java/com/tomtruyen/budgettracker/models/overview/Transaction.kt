package com.tomtruyen.budgettracker.models.overview

import java.util.*

class Transaction(var id : Int, val date: Date, val title: String, val price: Double, val isIncome: Boolean)