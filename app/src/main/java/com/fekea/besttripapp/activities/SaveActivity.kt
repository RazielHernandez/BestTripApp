package com.fekea.besttripapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityRouteBinding
import com.fekea.besttripapp.databinding.ActivitySaveBinding
import com.fekea.besttripapp.utils.FuelConsumption
import com.fekea.besttripapp.viewModel.RouteViewModel

class SaveActivity: AppCompatActivity() {

    companion object {
        const val TAG = "besttripapp.SaveActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivitySaveBinding
    private lateinit var route: TravelRoute
    private lateinit var routeModel: RouteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("search_route") as TravelRoute
        Log.e(TAG, "Route from ${route.startPoint.name} to ${route.endPoint.name}")
        Log.e(TAG, "Route: ${route.route}")

        routeModel = RouteViewModel(this)

        //placesRepo = PlacesRepo()
        //setupLiveDataListener()
        val reader = FuelConsumption(context1 = this, id1 = R.raw.fuelconsumptionratings)
        val gasConsumption = reader.fuelConsumptionSearch("2023","Cadillac","XT5")

        val routeName = findViewById<TextView>(R.id.route_title)
        routeName.text = "Travel to ${route.name}"

        val routeDistance = findViewById<TextView>(R.id.route_distance)
        routeDistance.text = "Distance: ${route.distanceString}"

        val routeDuration = findViewById<TextView>(R.id.route_duration)
        routeDuration.text = "Duration: ${route.durationString}"

        route.gasCost = gasConsumption.calculateCostConsumption(route.distance/1000)
        route.liters = gasConsumption.calculateLiterConsumption(route.distance/1000)
        val fuelConsumption = findViewById<TextView>(R.id.route_gas_consumption)
        val liters = gasConsumption.calculateLiterConsumptionString((route.distance/1000))
        val dollars = String.format("%.2f", gasConsumption.calculateCostConsumption((route.distance/1000)))

        fuelConsumption.text = "$liters / $ $dollars"

        Log.e(TAG, "GAS : ${gasConsumption.toString()}")

        val categoriesSpinner = findViewById<Spinner>(R.id.route_category)
        ArrayAdapter.createFromResource(
            this,
            R.array.route_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }

        val saveButton = findViewById<Button>(R.id.route_button_save)
        saveButton.setOnClickListener {
            routeModel.insertRouteToDB(route)
            val myIntent = Intent(this, RouteActivity::class.java)
            myIntent.putExtra("search_route", route)
            startActivity(myIntent)
        }

    }


}