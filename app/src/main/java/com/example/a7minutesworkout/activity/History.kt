package com.example.a7minutesworkout.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.a7minutesworkout.ui.adapter.HistoryAdapter
import com.example.a7minutesworkout.WorkOutApp
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import com.example.a7minutesworkout.viewmodel.HistoryViewModel

class History : AppCompatActivity() {

    private var binding : ActivityHistoryBinding? = null
    private var historyAdapter : HistoryAdapter? = null
    private val viewModel by viewModels<HistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WorkOutApp).apply {
            appComponent.inject(this@History)
            appComponent.inject(viewModel)
        }

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this@History
        lifecycle.addObserver(viewModel)

        setContentView(binding?.root)

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.apply {
            historyAdapter = HistoryAdapter()
            rvHistory.adapter = historyAdapter
        }
        viewModel.fetchAllDates()
        setupObservers()
    }

    private fun setupObservers() {
        binding?.lifecycleOwner?.let { lifecycleOwner ->
            viewModel.exerciseList.observe(lifecycleOwner) { list ->
                list?.let { historyAdapter?.updateItems(it) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}