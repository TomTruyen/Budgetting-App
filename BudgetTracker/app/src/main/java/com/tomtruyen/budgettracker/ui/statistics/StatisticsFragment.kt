package com.tomtruyen.budgettracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private lateinit var mAdapter: ArrayAdapter<CharSequence>
    private var mSelectedMonthPosition: Int = 0

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
                println(mAdapter.getItem(position).toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        actionBar?.customView = spinner

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}