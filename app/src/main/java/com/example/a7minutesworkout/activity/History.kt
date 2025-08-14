package com.example.a7minutesworkout.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.ui.adapter.HistoryAdapter
import com.example.a7minutesworkout.database.HistoryDao
import com.example.a7minutesworkout.WorkOutApp
import com.example.a7minutesworkout.database.HistoryDatabase
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import com.example.a7minutesworkout.model.HistoryEntity
import com.example.a7minutesworkout.viewmodel.HistoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }

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