package com.tomtruyen.budgettracker.ui.overview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentOverviewBinding
import com.tomtruyen.budgettracker.models.overview.BudgetAdapter
import com.tomtruyen.budgettracker.models.overview.Transaction
import com.tomtruyen.budgettracker.utils.Utils
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val mUtils : Utils = Utils()
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

        mAdapter = BudgetAdapter(context)
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = SlideInRightAnimator()

        binding.incomeBtn.setOnClickListener {
            openTransactionPage(true)
        }

        binding.expenseBtn.setOnClickListener {
            openTransactionPage(false)
        }

        mTransactionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mAdapter.notifyItemInserted(0)
                binding.recyclerView.scrollToPosition(0)

                updateBalance()
            }
        }

        updateBalance()

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


    private fun updateBalance() {
        var balance = 0.0
        mAdapter.databaseService.read().forEach {
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
}