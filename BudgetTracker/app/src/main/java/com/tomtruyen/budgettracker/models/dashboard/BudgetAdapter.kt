package com.tomtruyen.budgettracker.models.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R

class BudgetAdapter(private val mItems : List<ListItem>) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date)
        val titleText: TextView = itemView.findViewById(R.id.title)
        val priceText: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val listItem = inflater.inflate(R.layout.fragment_dashboard_list_item, parent, false)

        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: BudgetAdapter.ViewHolder, position: Int) {
        val item : ListItem = mItems[position]

        viewHolder.dateText.text = item.date.toString()
        viewHolder.titleText.text = item.title
        viewHolder.priceText.text = "$${item.price}"
    }

    override fun getItemCount(): Int {
        return mItems.size
    }


}