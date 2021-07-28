package com.tomtruyen.budgettracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.databinding.FragmentDashboardBinding
import com.tomtruyen.budgettracker.models.dashboard.BudgetAdapter
import com.tomtruyen.budgettracker.models.dashboard.ListItem
import com.tomtruyen.budgettracker.utils.Utils
import jp.wasabeef.recyclerview.adapters.*
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addItem(item: ListItem) {
        mItems.add(item)

        mAdapter.notifyItemInserted(mItems.lastIndex)
    }
}