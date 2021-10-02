package com.tomtruyen.budgettracker.overview

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        setHasOptionsMenu(true)

//        val actionBar = (activity as AppCompatActivity?)?.supportActionBar


        mAccountRecyclerView = view.findViewById(R.id.account_recyclerview)
        mAccountRecyclerView.adapter = activity?.let { AccountCardAdapter(it) }

        // Setup Horizontal RecyclerView
        mAccountRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        mTransactionRecyclerView = view.findViewById(R.id.transaction_recyclerview)
        mTransactionRecyclerView.adapter = activity?.let { TransactionAdapter(it, it) }
        mTransactionRecyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overview_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.btn_add_transaction -> {
            openTransactionActivity()

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openTransactionActivity() {
        val intent = Intent(activity, TransactionAddActivity::class.java)
        startActivity(intent)
    }
}