package com.tomtruyen.budgettracker.ui.statistics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentStatisticsBinding
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.tomtruyen.budgettracker.models.settings.Settings
import com.tomtruyen.budgettracker.models.statistics.StatisticsCategory
import com.tomtruyen.budgettracker.models.statistics.StatisticsCategoryAdapter
import com.tomtruyen.budgettracker.services.DatabaseService
import com.tomtruyen.budgettracker.utils.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import com.github.mikephil.charting.utils.Utils as ChartUtils


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private lateinit var mAdapter: ArrayAdapter<CharSequence>
    private lateinit var mStatisticsCategoryAdapter: StatisticsCategoryAdapter
    private lateinit var mDatabaseService: DatabaseService
    private lateinit var mBalanceTextView: TextView
    private var mSelectedMonthPosition: Int = Date().month
    private var mSelectedMonthTransactions: List<Transaction> = ArrayList()
    private var mSelectedMonthCategories: ArrayList<StatisticsCategory> = ArrayList()
    private val mUtils: Utils = Utils()
    private var mSettings: Settings = Settings.default()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowCustomEnabled(true)

        mDatabaseService = DatabaseService(requireContext())
        mSettings = mDatabaseService.readSettings()

        // Setup Spinner (Dropdown)
        val spinnerLayout =
            inflater.inflate(R.layout.month_dropdown, container, false)

        mAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month_array,
            R.layout.month_spinner_item
        )
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mBalanceTextView = spinnerLayout.findViewById(R.id.monthBalanceText)

        val spinner = spinnerLayout.findViewById<Spinner>(R.id.monthSpinner)
        spinner.adapter = mAdapter
        spinner.setSelection(mSelectedMonthPosition)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                mSelectedMonthPosition = position
                updateMonthTransactions()
                updateBalance()
                updateChart()
                checkEmptyDataset()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        actionBar?.customView = spinnerLayout

        // Setup RecycleView
        mStatisticsCategoryAdapter = StatisticsCategoryAdapter(
            requireContext(),
            mSelectedMonthCategories,
            mSettings,
            object : StatisticsCategoryAdapter.ItemClickListener {
                override fun onItemClick(category: StatisticsCategory) {
                    val intent = Intent(requireContext(), StatisticsCategoryActivity::class.java)
                    intent.putExtra("category", category.title)
                    intent.putExtra("month", mSelectedMonthPosition)
                    startActivity(intent)
                    activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
                }
            })

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = mStatisticsCategoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkEmptyDataset() {
        if (mSelectedMonthTransactions.isEmpty()) {
            binding.empty.visibility = View.VISIBLE
            binding.barChart.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.balanceSeparator.visibility = View.GONE
        } else {
            binding.empty.visibility = View.GONE
            binding.barChart.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
            binding.balanceSeparator.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateMonthTransactions() {
        val transactions = mDatabaseService.read()

        mSelectedMonthTransactions = transactions.filter {
            it.date.month == mSelectedMonthPosition
        }

        if (mSelectedMonthTransactions.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }

        mSelectedMonthCategories = ArrayList()
        val categories = resources.getStringArray(R.array.categories_array)
        categories.forEach {
            mSelectedMonthCategories.add(StatisticsCategory(it, 0.0))
        }
        mSelectedMonthCategories.add(StatisticsCategory("Income", 0.0))

        mSelectedMonthTransactions.forEach { transaction ->
            if (transaction.isIncome) {
                mSelectedMonthCategories.last().total += abs(transaction.price)
            } else {
                mSelectedMonthCategories.forEach {
                    if (it.title.lowercase() == transaction.category?.lowercase()) {
                        it.total += abs(transaction.price)
                    }
                }
            }
        }


        mStatisticsCategoryAdapter.mCategories = mSelectedMonthCategories
        mStatisticsCategoryAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun updateBalance() {
        var balance = 0.0

        mSelectedMonthCategories.forEach {
            if (it.title == "Income") {
                balance += it.total
            } else {
                balance -= it.total
            }
        }

        mBalanceTextView.text = mUtils.toCurrencyString(balance, mSettings.currencyLocale)
    }

    private fun updateChart() {
        val chart = binding.barChart


        val entries: ArrayList<BarEntry> = ArrayList()
        var isPopulated = false
        for (i in mSelectedMonthCategories.indices) {
            if (!isPopulated && mSelectedMonthCategories[i].total > 0) isPopulated = true
            entries.add(BarEntry(i.toFloat(), mSelectedMonthCategories[i].total.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter = BarFormatter()


        val colors = intArrayOf(
            R.color.purple,
            R.color.blue,
            R.color.red,
            R.color.pink,
            R.color.yellow,
            R.color.grey,
            R.color.green,
        )
        barDataSet.setColors(colors, requireContext())


        val data = BarData(barDataSet)
        chart.data = data

        chart.xAxis.valueFormatter = BarFormatter()
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.isGranularityEnabled = true
        chart.xAxis.granularity = 1f
        chart.xAxis.labelCount = mSelectedMonthCategories.size
        chart.setXAxisRenderer(
            CustomXAxisRenderer(
                chart.viewPortHandler,
                chart.xAxis,
                chart.getTransformer(YAxis.AxisDependency.LEFT)
            )
        )
        chart.xAxis.setDrawGridLines(false)

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        chart.extraBottomOffset = 25f

        if (!isPopulated) {
            chart.data = null
            chart.setNoDataText("")
        }
        chart.invalidate()
    }

    inner class BarFormatter : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            if (value == 0f) return ""

            return mUtils.toCurrencyString(value.toDouble(), mSettings.currencyLocale)
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()

            return if (index < mSelectedMonthCategories.size) {
                mSelectedMonthCategories[index].title
            } else {
                ""
            }
        }
    }

    inner class CustomXAxisRenderer(
        viewPortHandler: ViewPortHandler,
        xAxis: XAxis,
        trans: Transformer
    ) : XAxisRenderer(viewPortHandler, xAxis, trans) {
        override fun drawLabel(
            c: Canvas?,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF?,
            angleDegrees: Float
        ) {
            val line = formattedLabel.split("&").toTypedArray()
            ChartUtils.drawXAxisValue(
                c,
                line[0].trim(),
                x,
                y,
                mAxisLabelPaint,
                anchor,
                angleDegrees
            )
            if (line.size > 1) {
                ChartUtils.drawXAxisValue(
                    c,
                    line[1].trim(),
                    x,
                    y + mAxisLabelPaint.textSize,
                    mAxisLabelPaint,
                    anchor,
                    angleDegrees
                )
            }
        }
    }
}