package com.tomtruyen.budgettracker.ui.overview

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentOverviewBinding
import com.tomtruyen.budgettracker.models.overview.BudgetAdapter
import com.tomtruyen.budgettracker.models.overview.SwipeToDeleteCallback
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private lateinit var mAdapter: BudgetAdapter

    private lateinit var mTransactionResultLauncher: ActivityResultLauncher<Intent>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowCustomEnabled(true)
        val customActionBar =  inflater.inflate(R.layout.overview_actionbar, container, false)
        actionBar?.customView = customActionBar

        mAdapter = BudgetAdapter(context, customActionBar.findViewById(R.id.balanceText))
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = SlideInRightAnimator()

        // SwipeToDeleteCallback
        val icon: Drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!
        DrawableCompat.setTint(
            DrawableCompat.wrap(icon),
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        val background = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.red))

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(mAdapter, icon, background))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.incomeBtn.setOnClickListener {
            openTransactionPage(true)
        }

        binding.expenseBtn.setOnClickListener {
            openTransactionPage(false)
        }

        mTransactionResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    mAdapter.notifyItemInserted(0)
                    binding.recyclerView.scrollToPosition(0)

                    mAdapter.updateBalance()
                }
            }

        mAdapter.updateBalance()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openTransactionPage(isIncome: Boolean) {
        val intent = Intent(activity, TransactionActivity::class.java)
        intent.putExtra("isIncome", isIncome)
        mTransactionResultLauncher.launch(intent)
    }
}