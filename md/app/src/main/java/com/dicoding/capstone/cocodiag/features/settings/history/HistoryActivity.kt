package com.dicoding.capstone.cocodiag.features.settings.history


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.common.ResultState
import com.dicoding.capstone.cocodiag.databinding.ActivityHistoryBinding
import com.dicoding.capstone.cocodiag.features.ViewModelFactory
import com.dicoding.capstone.cocodiag.features.settings.SettingsViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding


    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }



    private fun setData() {
        viewModel.findHistory().observe(this) { result ->
            when(result) {
                is ResultState.Loading -> {

                }

                is ResultState.Error -> {
                }

                is ResultState.Success -> {
                    val rv: RecyclerView = binding.rvHistory
                    val adapter = HistoryAdapter(result.data.history)

                    rv.layoutManager = LinearLayoutManager(this)
                    rv.adapter = adapter
                }
            }
        }
    }
}