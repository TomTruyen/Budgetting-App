package com.tomtruyen.budgettracker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AccountCardAdapter(private val mContext: Context) : RecyclerView.Adapter<AccountCardAdapter.MyHolder>() {
    var mCardPageCount = 5

    companion object {
        private const val CARD = 0
        private const val ADD_CARD = 1
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mAccountName = itemView.findViewById<TextView>(R.id.account_name)
        var mAccountBalance = itemView.findViewById<TextView>(R.id.account_balance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        if(viewType == ADD_CARD) return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.card_add_fragment, parent, false))

        return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.card_fragment, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if(position == mCardPageCount - 1) return ADD_CARD

        return CARD
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if(holder.itemViewType == CARD) {
            holder.mAccountName.text = "Account #${position}"
        }
    }

    override fun getItemCount(): Int  = mCardPageCount
}
