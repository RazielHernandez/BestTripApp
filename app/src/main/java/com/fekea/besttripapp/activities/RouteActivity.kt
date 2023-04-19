package com.fekea.besttripapp.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.R
import com.fekea.besttripapp.adapters.PlaceSuggestedAdapter
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityRouteBinding
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.fekea.besttripapp.repository.PlacesRepo
import com.fekea.besttripapp.utils.FuelConsumption
import org.json.JSONObject

class RouteActivity: AppCompatActivity(), PlaceInterface {

    companion object {
        const val TAG = "besttripapp.RouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityRouteBinding
    //private lateinit var placesRepo: PlacesRepo
    private lateinit var placesAdapter: PlaceSuggestedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val route = intent.extras?.getSerializable("search_route") as TravelRoute
        Log.e(TAG, "Route from ${route.startPoint.name} to ${route.endPoint.name}")
        Log.e(TAG, "Route: ${route.route}")

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

        val fuelConsumption = findViewById<TextView>(R.id.route_gas_consumption)
        fuelConsumption.text = "Estimate gas: ${gasConsumption.calculateGasConsumptionString((route.distance/1000))}"



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

        placesAdapter = PlaceSuggestedAdapter(this)
        val recycleView = findViewById<RecyclerView>(R.id.places_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = placesAdapter


        //placesRepo.searchPlacesAround(context = this, route.endPoint, "Tourist", keyword = "Hotel")
        searchPlacesAround(route.endPoint, "Tourist", keyword = "Hotel")

    }

    /*private fun setupLiveDataListener() {
        placesRepo.placesListLiveData.observe(this) {
            Log.e(TAG, "${it.size} elements were recovered")
            for (place in it) {
                Log.e(TAG, "Place ${place.name}")
                Log.e(TAG, "Location ${place.location}")
                Log.e(TAG, "Image id: ${place.image}")
            }
        }
    }*/

    fun searchPlacesAround(location: TravelLocation, category: String, keyword: String) {

        var result: MutableList<TravelPlace> = mutableListOf()
        var urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?keyword=" + keyword +
                "&location=" + location.latitude.toString() + "," + location.longitude.toString() +
                "&radius=15000" +
                "&type=" + category +
                "&key=" + MAPS_API_KEY

        Log.e(TAG, urlQuery)


        val directionsRequest =
            object : StringRequest(Method.GET, urlQuery, Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                val places = jsonResponse.getJSONArray("results")

                for (c in 0 until places.length()) {
                    var newPlace = TravelPlace()
                    val actual = places.getJSONObject(c)
                    val placeLocation =
                        actual.getJSONObject("geometry").getJSONObject("location")
                    val photos = actual.getJSONArray("photos")

                    //Log.e(TAG, "PLACE: $actual")

                    newPlace.id = actual.getString("place_id")
                    newPlace.name = actual.getString("name")

                    newPlace.location.name = actual.getString("name")
                    newPlace.location.latitude = placeLocation.getDouble("lat")
                    newPlace.location.longitude = placeLocation.getDouble("lng")
                    newPlace.image = photos.getJSONObject(0).getString("photo_reference")

                    Log.e(TAG, newPlace.toString())
                    result.add(newPlace)


                }

                placesAdapter.setupAdapterData(result)

            }, Response.ErrorListener { _ ->
            }) {}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)

    }

    override fun onPlaceSelect(place: TravelPlace) {

    }


}