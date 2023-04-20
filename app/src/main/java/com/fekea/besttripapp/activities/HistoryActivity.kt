package com.fekea.besttripapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.R
import com.fekea.besttripapp.adapters.TravelHistoryAdapter
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityHistoryBinding
import com.fekea.besttripapp.interfaces.TravelInterface
import com.fekea.besttripapp.viewModel.RouteViewModel

class HistoryActivity: AppCompatActivity(), TravelInterface {

    companion object {
        const val TAG = "besttripapp.HistoryActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var routeModel: RouteViewModel
    private lateinit var routesAdapter: TravelHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        routeModel = RouteViewModel(this)

        val recycleView = findViewById<RecyclerView>(R.id.history_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this)
        routesAdapter = TravelHistoryAdapter(this, this)
        recycleView.adapter = routesAdapter

        routeModel.getRoutes()
        setupLiveDataListener()

    }

    private fun setupLiveDataListener() {
        routeModel.routesLiveData.observe(this) {
            Log.e(TAG, "${it.size} elements were recovered")
            routesAdapter.setupAdapterData(it)
        }
    }

    override fun onTravelPressed(travel: TravelRoute) {
        Log.e(TAG, "Click on route ${travel.name}")
    }
}