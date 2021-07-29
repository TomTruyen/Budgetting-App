package com.tomtruyen.budgettracker.models.overview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.services.DatabaseService
import com.tomtruyen.budgettracker.utils.Utils

class BudgetAdapter(private val mContext: Context?, private val mBalanceTextView: TextView) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>(){
    private val mUtils : Utils = Utils()
    private val databaseService : DatabaseService = DatabaseService(mContext)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.date)
        val titleText: TextView = itemView.findViewById(R.id.title)
        val priceText: TextView = itemView.findViewById(R.id.price)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val listItem = inflater.inflate(R.layout.fragment_overview_list_item, parent, false)

        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: BudgetAdapter.ViewHolder, position: Int) {
        val item : Transaction? = databaseService.readOne(position)

        if(item != null) {
            viewHolder.dateText.text = mUtils.toFormatString(item.date)
            viewHolder.titleText.text = item.title
            viewHolder.priceText.text = mUtils.toCurrencyString(item.price)

            if(mContext != null) {
                viewHolder.imageView.backgroundTintList = AppCompatResources.getColorStateList(mContext, R.color.green)

                if (item.isIncome) {
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
        }
    }

    override fun getItemCount(): Int {
        return databaseService.length()
    }

    fun delete(position: Int) {
        databaseService.delete(position)
        notifyItemRemoved(position)

        updateBalance()
    }

    fun updateBalance() {
        var balance = 0.0
        databaseService.read().forEach {
            if(it.isIncome) {
                balance += it.price
            } else {
                balance -= it.price
            }
        }

        mBalanceTextView.text = mUtils.toCurrencyString(balance)

        if(mContext != null) {
            if (balance >= 0) {
                mBalanceTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green))
            } else {
                mBalanceTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            }
        }
    }
}