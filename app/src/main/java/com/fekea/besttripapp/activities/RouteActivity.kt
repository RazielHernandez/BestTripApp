package com.fekea.besttripapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.MainActivity
import com.fekea.besttripapp.R
import com.fekea.besttripapp.adapters.PlaceSavedAdapter
import com.fekea.besttripapp.adapters.PlaceSuggestedAdapter
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityRouteBinding
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.fekea.besttripapp.viewModel.RouteViewModel
import org.json.JSONObject
import java.text.DecimalFormat

class RouteActivity: AppCompatActivity(), PlaceInterface {

    companion object {
        const val TAG = "besttripapp.RouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityRouteBinding
    private lateinit var placesAdapter: PlaceSavedAdapter
    private lateinit var route: TravelRoute
    private lateinit var routeModel: RouteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("search_route") as TravelRoute

        routeModel = RouteViewModel(this)

        placesAdapter = PlaceSavedAdapter(this, this)
        val recycleView = findViewById<RecyclerView>(R.id.places_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = placesAdapter

        val routeName = findViewById<TextView>(R.id.route_title)
        routeName.text = "Travel to ${route.name}"

        val routeDistance = findViewById<TextView>(R.id.route_distance)
        routeDistance.text = "Distance: ${route.distanceString}"

        val routeDuration = findViewById<TextView>(R.id.route_duration)
        routeDuration.text = "Duration: ${route.durationString}"

        val fuelConsumption = findViewById<TextView>(R.id.route_gas_consumption)
        val formatter = DecimalFormat("#.##")
        fuelConsumption.text = "${formatter.format(route.liters)} liters / $ ${formatter.format(route.gasCost)}"

        val placesButton = findViewById<Button>(R.id.route_button_places)
        placesButton.setOnClickListener {
            Log.e(TAG,"Sending route ${route._id} to places")
            val myIntent = Intent(this, PlacesActivity::class.java)
            myIntent.putExtra("route", route)
            startActivity(myIntent)
        }

        val deleteButton = findViewById<Button>(R.id.route_button_delete)
        deleteButton.setOnClickListener {
            Log.e(TAG,"Deleting route ${route._id} to places")
            routeModel.deleteRouteToDB(route)
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        placesAdapter.setupAdapterData(route.listOfPlaces)
    }



    override fun onPlaceSelect(place: TravelPlace) {
        placesAdapter.removeData(place)
        route.listOfPlaces.remove(place)
        routeModel.updateRouteToDB(route)
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