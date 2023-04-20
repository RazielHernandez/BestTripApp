package com.fekea.besttripapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fekea.besttripapp.databinding.ActivityHistoryBinding

class HistoryActivity: AppCompatActivity() {

    companion object {
        const val TAG = "besttripapp.HistoryActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}