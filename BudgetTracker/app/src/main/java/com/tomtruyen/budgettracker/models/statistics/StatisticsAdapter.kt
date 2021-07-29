package com.tomtruyen.budgettracker.models.statistics

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.tomtruyen.budgettracker.utils.Utils

class StatisticsAdapter(private val mContext: Context, var mTransactions: List<Transaction>) : RecyclerView.Adapter<StatisticsAdapter.ViewHolder>(){
    private val mUtils : Utils = Utils()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date)
        val titleText: TextView = itemView.findViewById(R.id.title)
        val priceText: TextView = itemView.findViewById(R.id.price)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val listItem = inflater.inflate(R.layout.fragment_overview_list_item, parent, false)

        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: StatisticsAdapter.ViewHolder, position: Int) {
        val item : Transaction = mTransactions[position]

        viewHolder.dateText.text = mUtils.toFormatString(item.date)
        viewHolder.titleText.text = item.title
        viewHolder.priceText.text = mUtils.toCurrencyString(item.price)

        if (item.isIncome) {
            viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.green)

            viewHolder.priceText.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.green
                )
            )

            viewHolder.imageView.setImageResource(R.drawable.ic_money)
        } else {
            viewHolder.priceText.setTextColor(ContextCompat.getColor(mContext, R.color.red))

            when (item.category) {
                "Home & Utilities" -> {
                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.purple)
                    viewHolder.imageView.setImageResource(R.drawable.ic_home)
                }
                "Travel" -> {
                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.blue)
                    viewHolder.imageView.setImageResource(R.drawable.ic_travel)
                }
                "Fitness & Health" -> {
                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.red)
                    viewHolder.imageView.rotation = 315f
                    viewHolder.imageView.setImageResource(R.drawable.ic_health)
                }
                "Food & Drinks" -> {
                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.pink)
                    viewHolder.imageView.setImageResource(R.drawable.ic_food)
                }
                "Investment" -> {
                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.yellow)
                    viewHolder.imageView.setImageResource(R.drawable.ic_investment)
                }
                else -> {
                    val scale = mContext.resources.displayMetrics.density
                    val padding = Utils().densityToPixels(12, scale)

                    viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.grey)
                    viewHolder.imageView.setPadding(padding, padding, padding, padding)
                    viewHolder.imageView.setImageResource(R.drawable.ic_other)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mTransactions.size
    }
}

