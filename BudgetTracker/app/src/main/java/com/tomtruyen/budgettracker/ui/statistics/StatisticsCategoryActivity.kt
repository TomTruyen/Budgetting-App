package com.tomtruyen.budgettracker.ui.statistics

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.tomtruyen.budgettracker.models.settings.Settings
import com.tomtruyen.budgettracker.models.statistics.StatisticsAdapter
import com.tomtruyen.budgettracker.services.DatabaseService
import java.util.*
import kotlin.collections.ArrayList


class StatisticsCategoryActivity : AppCompatActivity() {
    private lateinit var mStatisticsAdapter: StatisticsAdapter
    private lateinit var mDatabaseService: DatabaseService
    private var mSettings = Settings.default()
    private var mTransactions: List<Transaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_category)

        val category = intent.getStringExtra("category")
        val month = intent.getIntExtra("month", Date().month)

        supportActionBar?.title = category
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        mDatabaseService = DatabaseService(this)

        mSettings = mDatabaseService.readSettings()

        mTransactions = getTransactions(category, month)

        mStatisticsAdapter = StatisticsAdapter(this, mTransactions, mSettings)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = mStatisticsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if(mTransactions.isEmpty()) {
            findViewById<TextView>(R.id.empty).visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            findViewById<TextView>(R.id.empty).visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    // Go back to StatisticsFragment (when using manifest 'ParentActivity' it would go back to the OverView fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
            overridePendingTransition(R.anim.enter_child, R.anim.exit_child)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTransactions(category: String?, month: Int): List<Transaction> {
        if (category == null) {
            return ArrayList()
        }

        val transactions = mDatabaseService.read()

        return transactions.filter {
            month == it.date.month && ((category == "Income" && it.isIncome) || (category.lowercase() == it.category?.lowercase()))
        }
    }
}