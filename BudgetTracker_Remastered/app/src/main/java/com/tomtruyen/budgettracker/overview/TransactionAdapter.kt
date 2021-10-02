package com.tomtruyen.budgettracker.overview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R

class TransactionAdapter(private val mContext: Context, private val mActivity: Activity) : RecyclerView.Adapter<TransactionAdapter.MyHolder>() {
    private var mTransactionCount = 20

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTransactionTitle : TextView = itemView.findViewById(R.id.transaction_title)
        val mTransactionBalance: TextView = itemView.findViewById(R.id.transaction_price)
        val mTransactionButton : Button = itemView.findViewById(R.id.transaction_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.transaction_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.mTransactionTitle.text = "Transaction #${position}"

        holder.mTransactionButton.setOnClickListener {
            val intent = Intent(mContext, TransactionActivity::class.java)
            mContext.startActivity(intent)
            mActivity.overridePendingTransition(R.anim.enter, R.anim.exit)
        }
    }

    override fun getItemCount(): Int {
        return mTransactionCount
    }
}