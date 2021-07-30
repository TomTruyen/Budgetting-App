package com.tomtruyen.budgettracker.ui.settings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tomtruyen.budgettracker.R
import com.tomtruyen.budgettracker.databinding.FragmentSettingsBinding
import com.tomtruyen.budgettracker.models.settings.Settings
import com.tomtruyen.budgettracker.models.settings.SettingsAdapter
import com.tomtruyen.budgettracker.services.DatabaseService
import com.tomtruyen.budgettracker.utils.Utils
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private lateinit var mSettingsAdapter: SettingsAdapter
    private val mUtils = Utils()
    private lateinit var mDatabaseService: DatabaseService
    private var mSettings : Settings = Settings.default()

    private val mLocaleList: List<Locale> = listOf(
        Locale.US,
        Locale.GERMANY,
        Locale.UK,
        Locale.JAPAN,
        Locale.SIMPLIFIED_CHINESE,
    )

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(false)

        mDatabaseService = DatabaseService(requireContext())
        mSettings = mDatabaseService.readSettings()

        mSettingsAdapter = SettingsAdapter(requireContext())

        val listview = binding.settingsListView
        listview.adapter = mSettingsAdapter

        listview.setOnItemClickListener { _ : AdapterView<*>, _: View, position: Int, _ ->
            val setting = mSettingsAdapter.getItem(position)

            when(setting.title.lowercase()) {
                "currency" -> openCurrencyDialog()

            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun openCurrencyDialog() {
        val dialog = Dialog(requireContext())

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.spinner_dialog_layout)

        val spinner = dialog.findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mUtils.toCurrencyDisplayNames(mLocaleList))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(mLocaleList.indexOf(mSettings.currencyLocale))

        val submitButton = dialog.findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val selectedPosition = spinner.selectedItemPosition
            val selectedLocale = mLocaleList[selectedPosition]

            mSettings.currencyLocale = selectedLocale
            mDatabaseService.saveSettings(mSettings)

            Snackbar.make(requireView(), "Currency saved", Snackbar.LENGTH_SHORT).show()

            dialog.dismiss()
        }

        dialog.create()
        dialog.show()
    }
}