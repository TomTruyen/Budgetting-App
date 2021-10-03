package com.tomtruyen.budgettracker.overview

import android.content.Context
import android.opengl.Visibility
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tomtruyen.budgettracker.R

class TransactionAddAdapter(mContext: Context) : ArrayAdapter<TransactionCategory>(mContext, 0, TransactionCategory.values())  {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: mLayoutInflater.inflate(R.layout.category_item, parent, false)

        getItem(position)?.let { category -> setItemForCategory(view, category) }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if(position == 0) {
            view = mLayoutInflater.inflate(R.layout.category_item_header, parent, false)
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        } else {
            view = mLayoutInflater.inflate(R.layout.category_item, parent, false)
            view.findViewById<ImageView>(R.id.image_dropdown).visibility = View.GONE
            getItem(position)?.let { category -> setItemForCategory(view, category) }
        }


        return view
    }

    override fun getItem(position: Int): TransactionCategory? {
        if(position == 0) return null

        return super.getItem(position - 1)
    }

    override fun isEnabled(position: Int): Boolean = position != 0

    private fun setItemForCategory(view: View, category: TransactionCategory) {
        val imageCategory = view.findViewById<ImageView>(R.id.image_category)
        val textCategory = view.findViewById<TextView>(R.id.text_category)

        imageCategory.setBackgroundResource(category.icon)
        textCategory.text = category.title
    }
}