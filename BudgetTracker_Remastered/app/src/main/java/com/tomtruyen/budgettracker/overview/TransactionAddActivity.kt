package com.tomtruyen.budgettracker.overview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import com.tomtruyen.budgettracker.R

class TransactionAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.transaction_add_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.btn_add_transaction -> {
            addTransaction()
            true
        }

        else -> { super.onOptionsItemSelected(item) }
    }

    private fun addTransaction() {
        val title = findViewById<EditText>(R.id.edit_text_title).text
        val price = findViewById<EditText>(R.id.edit_text_price).text
        val isIncome = findViewById<SwitchCompat>(R.id.switch_type).isChecked

        // TODO
        // Add Transaction to Database & refresh transactions on OverviewFragment

        finish()
    }
}