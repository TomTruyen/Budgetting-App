package com.tomtruyen.budgettracker.overview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R

class AccountCardAdapter(private val mContext: Context) : RecyclerView.Adapter<AccountCardAdapter.MyHolder>() {
    private var mCardPageCount = 5

    companion object {
        private const val CARD = 0
        private const val ADD_CARD = 1
    }

    inner class MyHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var mAccountName : TextView? = null
        var mAccountBalance : TextView? = null

        var mAccountAddButton: Button? = null

        init {
            if(viewType == CARD) {
                mAccountName = itemView.findViewById(R.id.account_name)
                mAccountBalance = itemView.findViewById(R.id.account_balance)
            } else {
                mAccountAddButton = itemView.findViewById(R.id.account_add_button)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        if(viewType == ADD_CARD) return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.card_add_item, parent, false), viewType)

        return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false), viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if(position == mCardPageCount - 1) return ADD_CARD

        return CARD
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if(holder.itemViewType == CARD) {
            holder.mAccountName?.text = "Account #${position}"
        } else {
            holder.mAccountAddButton?.setOnClickListener {
                val intent = Intent(mContext, AccountAddActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int  = mCardPageCount
}
