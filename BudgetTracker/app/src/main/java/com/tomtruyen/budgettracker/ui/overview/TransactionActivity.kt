package com.tomtruyen.budgettracker.ui.overview

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.tomtruyen.budgettracker.services.DatabaseService
import java.util.*

class TransactionActivity : AppCompatActivity() {
    private var isIncome: Boolean = true
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Get extra
        isIncome = intent.getBooleanExtra("isIncome", true)

        // Set ActionBarTitle
        supportActionBar?.title = "Add ${if (isIncome) "income" else "expense"}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        // Setup Dropdown
        spinner = findViewById(R.id.categorySpinner)
        if (isIncome) {
            spinner.visibility = View.GONE
        } else {
            val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Set onClickListener
        findViewById<FloatingActionButton>(R.id.submitButton).setOnClickListener { saveTransaction() }
    }

    private fun saveTransaction() {
        if (isValid()) {
            val label =
                findViewById<TextInputLayout>(R.id.titleInputLayout).editText?.text.toString()
            val amount =
                findViewById<TextInputLayout>(R.id.priceInputLayout).editText?.text.toString()
                    .toDouble()
            val category = if (isIncome) "" else spinner.selectedItem.toString()

            val transaction = Transaction(-1, Date(), label, category, amount, isIncome)

            val databaseService = DatabaseService(this)
            databaseService.save(transaction)

            setResult(RESULT_OK)
            finish()
        }
    }

    private fun isValid(): Boolean {
        val labelTextLayout = findViewById<TextInputLayout>(R.id.titleInputLayout)
        val labelText = labelTextLayout.editText?.text.toString()

        if (labelText == "") {
            labelTextLayout.error = "Label can't be empty"
            return false
        }

        labelTextLayout.isErrorEnabled = false

        val priceTextLayout = findViewById<TextInputLayout>(R.id.priceInputLayout)
        val priceText = priceTextLayout.editText?.text.toString()

        if (priceText == "") {
            priceTextLayout.error = "Amount can't be empty"
            return false
        }

        val price = priceText.toDouble()
        if (price <= 0) {
            priceTextLayout.error = "Amount must be greater than 0"
            return false
        }

        priceTextLayout.isErrorEnabled = false

        return true
    }
}