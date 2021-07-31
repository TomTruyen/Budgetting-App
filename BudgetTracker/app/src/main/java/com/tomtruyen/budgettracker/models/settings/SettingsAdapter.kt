package com.tomtruyen.budgettracker.models.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tomtruyen.budgettracker.R

class SettingsAdapter(private val mContext: Context) : BaseAdapter() {
    private val mSettingsList = ArrayList<SettingItem>()

    init {
        val currencySetting = SettingItem("Currency", "Select what currency should be used")
        val monthlyBudgetSetting = SettingItem("Monthly Budget", "Set your monthly spending limit")

        mSettingsList.add(currencySetting)
        mSettingsList.add(monthlyBudgetSetting)
    }

    override fun getCount(): Int {
        return mSettingsList.size
    }

    override fun getItem(position: Int): SettingItem {
        return mSettingsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = if (convertView == null) {
            val inflater: LayoutInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.settings_item, parent, false)
        } else {
            convertView
        }

        val settingItem: SettingItem = getItem(position)

        view.findViewById<TextView>(R.id.setting_title).text = settingItem.title
        view.findViewById<TextView>(R.id.setting_subtitle).text = settingItem.subtitle

        return view
    }
}