package com.tomtruyen.budgettracker.models.overview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.utils.Utils

class BudgetAdapter(private val mItems : List<ListItem>, private val mContext: Context?) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>(){
    private val mUtils : Utils = Utils()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date)
        val titleText: TextView = itemView.findViewById(R.id.title)
        val priceText: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val listItem = inflater.inflate(R.layout.fragment_overview_list_item, parent, false)

        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: BudgetAdapter.ViewHolder, position: Int) {
        val item : ListItem = mItems[position]



        viewHolder.dateText.text = mUtils.toFormatString(item.date)
        viewHolder.titleText.text = item.title
        viewHolder.priceText.text = mUtils.toCurrencyString(item.price)
        if(mContext != null) {
            if(item.isIncome) {
                viewHolder.priceText.setTextColor(ContextCompat.getColor(mContext, R.color.green))
            } else {
                viewHolder.priceText.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            }
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }
}