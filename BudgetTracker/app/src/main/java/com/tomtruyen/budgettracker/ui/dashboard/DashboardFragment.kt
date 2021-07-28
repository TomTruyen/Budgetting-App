package com.tomtruyen.budgettracker.ui.dashboard

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentDashboardBinding
import com.tomtruyen.budgettracker.models.dashboard.BudgetAdapter
import com.tomtruyen.budgettracker.models.dashboard.ListItem
import com.tomtruyen.budgettracker.utils.Utils
import jp.wasabeef.recyclerview.adapters.*
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val mUtils : Utils = Utils()
    private lateinit var mAdapter: BudgetAdapter
    private var mItems : ArrayList<ListItem> = arrayListOf(ListItem(Date(), "Food", 125.00, false), ListItem(Date(), "Salary", 375.00, true))

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        mAdapter = BudgetAdapter(mItems, context)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = SlideInRightAnimator()

        binding.incomeBtn.setOnClickListener {
            addItem(ListItem(Date(), "Test Income", 25.0, true))
        }

        binding.expenseBtn.setOnClickListener {
            addItem(ListItem(Date(), "Test Expense", 100.0, false))
        }

        updateBalance()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateBalance() {
        var balance = 0.0
        mItems.forEach {
            if(it.isIncome) {
                balance += it.price
            } else {
                balance -= it.price
            }
        }

        binding.balanceText.text = mUtils.toCurrencyString(balance)

        if (balance >= 0) {
            binding.balanceText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        } else {
            binding.balanceText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
    }

    private fun addItem(item: ListItem) {
        mItems.add(item)

        mAdapter.notifyItemInserted(mItems.lastIndex)

        binding.recyclerView.scrollToPosition(mItems.lastIndex)

        updateBalance()
    }
}