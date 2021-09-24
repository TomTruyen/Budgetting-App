package com.tomtruyen.budgettracker

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class CardSlidePagerAdapter(private val mContext: Context) : RecyclerView.Adapter<CardSlidePagerAdapter.MyHolder>() {
    var mCardPageCount = 5

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mAccountName = itemView.findViewById<TextView>(R.id.account_name)
        var mAccountBalance = itemView.findViewById<TextView>(R.id.account_balance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.card_fragment, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.mAccountName.text = "Account #${position}"
    }

    override fun getItemCount(): Int  = mCardPageCount
}
