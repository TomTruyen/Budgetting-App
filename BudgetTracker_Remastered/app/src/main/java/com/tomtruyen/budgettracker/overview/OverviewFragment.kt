package com.tomtruyen.budgettracker.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nambimobile.widgets.efab.FabOption
import com.tomtruyen.budgettracker.R

class OverviewFragment : Fragment() {
    private lateinit var mAccountRecyclerView: RecyclerView
    private lateinit var mTransactionRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        mAccountRecyclerView = view.findViewById(R.id.account_recyclerview)
        mAccountRecyclerView.adapter = activity?.let { AccountCardAdapter(it) }

        // Setup Horizontal RecyclerView
        mAccountRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        mTransactionRecyclerView = view.findViewById(R.id.transaction_recyclerview)
        mTransactionRecyclerView.adapter = activity?.let { TransactionAdapter(it, it) }
        mTransactionRecyclerView.layoutManager = LinearLayoutManager(activity)

        view.findViewById<FabOption>(R.id.btn_income).setOnClickListener { openTransactionActivity(true) }
        view.findViewById<FabOption>(R.id.btn_expense).setOnClickListener { openTransactionActivity(false) }

        return view
    }

    private fun openTransactionActivity(isIncome: Boolean) {
        val intent = Intent(activity, TransactionAddActivity::class.java)
        intent.putExtra("isIncome", isIncome)
        startActivity(intent)
    }
}