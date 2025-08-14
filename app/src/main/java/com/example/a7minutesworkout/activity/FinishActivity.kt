package com.example.a7minutesworkout.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.database.HistoryDao
import com.example.a7minutesworkout.WorkOutApp
import com.example.a7minutesworkout.database.HistoryDatabase
import com.example.a7minutesworkout.databinding.ActivityFinishBinding
import com.example.a7minutesworkout.model.HistoryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FinishActivity : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.buttonFinish?.setOnClickListener {
            backToMainActivity()
        }

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        addDateToDatabase()
    }

    private fun backToMainActivity(){
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun addDateToDatabase(){
        val c = Calendar.getInstance()
        val dateTime = c.time

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

//        lifecycleScope.launch {
//            historyDatabase.historyDao().insert(HistoryEntity(date))
//        }
    }
}