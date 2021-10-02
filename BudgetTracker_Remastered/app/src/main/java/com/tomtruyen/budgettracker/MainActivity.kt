package com.tomtruyen.budgettracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nambimobile.widgets.efab.FabOption


class MainActivity : AppCompatActivity() {
    private lateinit var mAccountRecyclerView: RecyclerView
    private lateinit var mTransactionRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAccountRecyclerView = findViewById(R.id.account_recyclerview)
        mAccountRecyclerView.adapter = AccountCardAdapter(this)

        // Setup Horizontal RecyclerView
        mAccountRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mTransactionRecyclerView = findViewById(R.id.transaction_recyclerview)
        mTransactionRecyclerView.adapter = TransactionAdapter(this, this)
        mTransactionRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<FabOption>(R.id.btn_income).setOnClickListener { openTransactionActivity(true) }
        findViewById<FabOption>(R.id.btn_expense).setOnClickListener { openTransactionActivity(false) }
    }

    fun openTransactionActivity(isIncome: Boolean) {
        val intent = Intent(this, TransactionAddActivity::class.java)
        intent.putExtra("isIncome", isIncome)
        startActivity(intent)
    }
}
