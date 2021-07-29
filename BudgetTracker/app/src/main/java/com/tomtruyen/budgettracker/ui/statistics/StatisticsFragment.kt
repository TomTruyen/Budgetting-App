package com.tomtruyen.budgettracker.ui.statistics

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.github.mikephil.charting.utils.Utils as ChartUtils
import com.tomtruyen.budgettracker.models.statistics.ChartItem
import com.tomtruyen.budgettracker.utils.Utils


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private lateinit var mAdapter: ArrayAdapter<CharSequence>
    private lateinit var mDatabaseService : DatabaseService
    private var mSelectedMonthPosition: Int = 0
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

        val spinner : Spinner = inflater.inflate(R.layout.month_dropdown, container, false) as Spinner
        mAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.month_array,
            R.layout.month_spinner_item
        )
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                mSelectedMonthPosition = position
                updateChart()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        actionBar?.customView = spinner

        mDatabaseService = DatabaseService(requireContext())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateChart() {
        val chart = binding.barChart

        val transactions = mDatabaseService.read()

        val monthTransactions = transactions.filter {
            it.date.month == mSelectedMonthPosition
        }

        mChartItems = ArrayList()
        val categories = resources.getStringArray(R.array.categories_array)
        categories.forEach { category ->
            val categoryTransactions = monthTransactions.filter { it.category.equals(category) && !it.isIncome}

            val totalSpent = categoryTransactions.sumOf { abs(it.price) }

            mChartItems.add(ChartItem(category, totalSpent))
        }

        val entries: ArrayList<BarEntry> = ArrayList()
        for(i in mChartItems.indices) {
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
        chart.setXAxisRenderer(CustomXAxisRenderer(chart.viewPortHandler, chart.xAxis, chart.getTransformer(YAxis.AxisDependency.LEFT)))
        chart.xAxis.setDrawGridLines(false)

        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        chart.extraBottomOffset = 25f

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