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
import com.tomtruyen.budgettracker.models.settings.Settings
import com.tomtruyen.budgettracker.utils.Utils

class StatisticsCategoryAdapter(
    private val mContext: Context,
    var mCategories: List<StatisticsCategory>,
    private val mSettings: Settings,
    private val mItemListener: ItemClickListener
) : RecyclerView.Adapter<StatisticsCategoryAdapter.ViewHolder>() {
    private val mUtils: Utils = Utils()

    interface ItemClickListener {
        fun onItemClick(category: StatisticsCategory)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title)
        val priceText: TextView = itemView.findViewById(R.id.price)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatisticsCategoryAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val listItem =
            inflater.inflate(R.layout.fragment_statistics_category_list_item, parent, false)

        return ViewHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: StatisticsCategoryAdapter.ViewHolder, position: Int) {
        val category: StatisticsCategory = mCategories[position]

        viewHolder.titleText.text = category.title
        viewHolder.priceText.text =
            mUtils.toCurrencyString(category.total, mSettings.currencyLocale)

        viewHolder.itemView.setOnClickListener {
            mItemListener.onItemClick(mCategories[position])
        }

        if (category.title == "Income") {
            viewHolder.priceText.setTextColor(ContextCompat.getColor(mContext, R.color.green))
            viewHolder.imageView.backgroundTintList =
                AppCompatResources.getColorStateList(mContext, R.color.green)
            viewHolder.imageView.setImageResource(R.drawable.ic_money)
        } else {
            viewHolder.priceText.setTextColor(ContextCompat.getColor(mContext, R.color.red))

            when (category.title) {
                "Home & Utilities" -> {
                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.purple)
                    viewHolder.imageView.setImageResource(R.drawable.ic_home)
                }
                "Travel" -> {
                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.blue)
                    viewHolder.imageView.setImageResource(R.drawable.ic_travel)
                }
                "Fitness & Health" -> {
                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.red)
                    viewHolder.imageView.rotation = 315f
                    viewHolder.imageView.setImageResource(R.drawable.ic_health)
                }
                "Food & Drinks" -> {
                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.pink)
                    viewHolder.imageView.setImageResource(R.drawable.ic_food)
                }
                "Investment" -> {
                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.yellow)
                    viewHolder.imageView.setImageResource(R.drawable.ic_investment)
                }
                else -> {
                    val scale = mContext.resources.displayMetrics.density
                    val padding = Utils().densityToPixels(12, scale)

                    viewHolder.imageView.backgroundTintList =
                        AppCompatResources.getColorStateList(mContext, R.color.grey)
                    viewHolder.imageView.setPadding(padding, padding, padding, padding)
                    viewHolder.imageView.setImageResource(R.drawable.ic_other)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mCategories.size
    }
}