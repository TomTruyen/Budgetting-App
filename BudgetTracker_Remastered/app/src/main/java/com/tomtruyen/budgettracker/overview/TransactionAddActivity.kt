package com.tomtruyen.budgettracker.overview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tomtruyen.budgettracker.R

class TransactionAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }
}