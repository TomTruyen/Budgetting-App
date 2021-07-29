package com.tomtruyen.budgettracker.ui.statistics

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.utils.ColorTemplate
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentStatisticsBinding
import com.tomtruyen.budgettracker.services.DatabaseService
import kotlin.math.abs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.tomtruyen.budgettracker.models.overview.BudgetAdapter
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.github.mikephil.charting.utils.Utils as ChartUtils
import com.tomtruyen.budgettracker.models.statistics.ChartItem
import com.tomtruyen.budgettracker.models.statistics.StatisticsAdapter
import com.tomtruyen.budgettracker.utils.Utils
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*
import kotlin.collections.ArrayList


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private lateinit var mAdapter: ArrayAdapter<CharSequence>
    private lateinit var mStatisticsAdapter: StatisticsAdapter
    private lateinit var mDatabaseService : DatabaseService
    private var mSelectedMonthPosition: Int = Date().month
    private var mSelectedMonthTransactions: List<Transaction> = ArrayList()
    private var mChartItems : ArrayList<ChartItem> = ArrayList()
    private val mUtils : Utils = Utils()

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

        // Setup Spinner (Dropdown)
        val spinner : Spinner = inflater.inflate(R.layout.month_dropdown, container, false) as Spinner
        mAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month_array,
            R.layout.month_spinner_item
        )
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                updateChart()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        actionBar?.customView = spinner

        // Setup RecycleView
        mStatisticsAdapter = StatisticsAdapter(requireContext(), mSelectedMonthTransactions)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = mStatisticsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateMonthTransactions() {
        val transactions = mDatabaseService.read()

        mSelectedMonthTransactions = transactions.filter {
            it.date.month == mSelectedMonthPosition
        }

        if(mSelectedMonthTransactions.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }

        mStatisticsAdapter.mTransactions = mSelectedMonthTransactions
        mStatisticsAdapter.notifyDataSetChanged()
    }

    private fun updateChart() {
        val chart = binding.barChart

        mChartItems = ArrayList()
        val categories = resources.getStringArray(R.array.categories_array)
        categories.forEach { category ->
            val categoryTransactions = mSelectedMonthTransactions.filter { it.category.equals(category) && !it.isIncome}

            val totalSpent = categoryTransactions.sumOf { abs(it.price) }

            mChartItems.add(ChartItem(category, totalSpent))
        }

        val entries: ArrayList<BarEntry> = ArrayList()
        var isPopulated = false
        for(i in mChartItems.indices) {
            if(!isPopulated && mChartItems[i].value > 0) isPopulated = true
            entries.add(BarEntry(i.toFloat(), mChartItems[i].value.toFloat()))
        }


            val barDataSet = BarDataSet(entries, "")
            barDataSet.valueTextSize = 12f
            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
            barDataSet.valueFormatter = BarFormatter()

            val data = BarData(barDataSet)
            chart.data = data

            chart.xAxis.valueFormatter = BarFormatter()
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.xAxis.isGranularityEnabled = true;
            chart.xAxis.granularity = 1f
            chart.xAxis.labelCount = categories.size
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

        if(!isPopulated) {
            chart.data = null
            chart.setNoDataText("No expenses found this month.")
            chart.setNoDataTextColor(R.color.red)
        }
            chart.invalidate()
    }

    inner class BarFormatter : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            if(value == 0f) return ""

            return mUtils.toCurrencyString(value.toDouble())
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()

            return if (index < mChartItems.size) {
                mChartItems[index].label
            } else {
                ""
            }
        }
    }

    inner class CustomXAxisRenderer(viewPortHandler: ViewPortHandler, xAxis: XAxis, trans: Transformer) : XAxisRenderer(viewPortHandler, xAxis, trans) {
        override fun drawLabel(
            c: Canvas?,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF?,
            angleDegrees: Float
        ) {
            val line = formattedLabel.split("&").toTypedArray()
            ChartUtils.drawXAxisValue(c, line[0].trim(), x, y, mAxisLabelPaint, anchor, angleDegrees)
            if(line.size > 1) {
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