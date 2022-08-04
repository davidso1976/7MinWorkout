package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class History : AppCompatActivity() {
    private var binding : ActivityHistoryBinding ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        val historyDao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(historyDao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDatesList ->
                setupListOfDatesIntoRecyclerView(ArrayList(allCompletedDatesList))
            }
        }
    }

    private fun setupListOfDatesIntoRecyclerView(list : ArrayList<HistoryEntity>){
        if(list.isNotEmpty()){
            val historyAdapter = HistoryAdapter(list)
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this@History)
            binding?.rvHistory?.adapter = historyAdapter
            binding?.rvHistory?.visibility = View.VISIBLE
            binding?.tvNoDataAvailable?.visibility = View.GONE
            binding?.tvHistory?.visibility = View.VISIBLE

        } else {
            binding?.rvHistory?.visibility = View.GONE
            binding?.tvNoDataAvailable?.visibility = View.VISIBLE
            binding?.tvHistory?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}