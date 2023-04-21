package com.fekea.besttripapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.MainActivity
import com.fekea.besttripapp.R
import com.fekea.besttripapp.adapters.TravelHistoryAdapter
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityHistoryBinding
import com.fekea.besttripapp.interfaces.TravelInterface
import com.fekea.besttripapp.viewModel.RouteViewModel

class HistoryActivity: AppCompatActivity(), TravelInterface {

    companion object {
        const val TAG = "besttripapp.HistoryActivity"
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
        val myIntent = Intent(this, RouteActivity::class.java)
        myIntent.putExtra("search_route", travel)
        startActivity(myIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.item_home) {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            return true
        }
        if (id == R.id.item_user) {
            Toast.makeText(this, "Item user Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.item_history) {
            val myIntent = Intent(this, HistoryActivity::class.java)
            startActivity(myIntent)
            return true
        }
        if (id == R.id.item_vehicles) {
            val myIntent = Intent(this, SearchRouteActivity::class.java)
            startActivity(myIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}