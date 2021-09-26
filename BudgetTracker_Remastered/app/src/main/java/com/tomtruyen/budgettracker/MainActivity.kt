package com.tomtruyen.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


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

        // Setup Vertical RecyclerView
//        mTransactionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
