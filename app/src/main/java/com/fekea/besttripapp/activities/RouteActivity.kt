package com.fekea.besttripapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import com.fekea.besttripapp.databinding.ActivitySaveBinding
import com.fekea.besttripapp.interfaces.PlaceInterface
import org.json.JSONObject
import java.nio.channels.Selector
import java.text.DecimalFormat

class RouteActivity: AppCompatActivity(), PlaceInterface, OnItemSelectedListener {

    companion object {
        const val TAG = "besttripapp.RouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityRouteBinding
    private lateinit var placesAdapter: PlaceSuggestedAdapter
    private lateinit var route: TravelRoute
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("search_route") as TravelRoute
        Log.e(SaveActivity.TAG, "Route from ${route.startPoint.name} to ${route.endPoint.name}")
        Log.e(SaveActivity.TAG, "Route: ${route.route}")

        placesAdapter = PlaceSuggestedAdapter(this, this)
        val recycleView = findViewById<RecyclerView>(R.id.places_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = placesAdapter

        val categoriesSpinner: Spinner = findViewById(R.id.route_place_category)
        ArrayAdapter.createFromResource(
            this,
            R.array.place_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }

        categoriesSpinner.onItemSelectedListener = this

        val routeName = findViewById<TextView>(R.id.route_title)
        routeName.text = "Travel to ${route.name}"

        val routeDistance = findViewById<TextView>(R.id.route_distance)
        routeDistance.text = "Distance: ${route.distanceString}"

        val routeDuration = findViewById<TextView>(R.id.route_duration)
        routeDuration.text = "Duration: ${route.durationString}"

        val fuelConsumption = findViewById<TextView>(R.id.route_gas_consumption)
        val formatter = DecimalFormat("#.##")
        fuelConsumption.text = "${formatter.format(route.liters)} liters / $ ${formatter.format(route.gasCost)}"

        category = "Restaurant"
        searchPlacesAround(route.endPoint, category, category)
    }

    fun searchPlacesAround(location: TravelLocation, category: String, keyword: String) {


        var result: MutableList<TravelPlace> = mutableListOf()
        var urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                //"?keyword=" + keyword +
                "?location=" + location.latitude.toString() + "," + location.longitude.toString() +
                "&radius=15000" +
                "&type=" + category +
                "&key=" + SaveActivity.MAPS_API_KEY

        Log.e(TAG, urlQuery)


        val directionsRequest =
            object : StringRequest(Method.GET, urlQuery, Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.getString("status") == "OK"){
                    val places = jsonResponse.getJSONArray("results")

                    for (c in 0 until places.length()) {
                        var newPlace = TravelPlace()
                        val actual = places.getJSONObject(c)
                        val placeLocation =
                            actual.getJSONObject("geometry").getJSONObject("location")
                        if (actual.has("photos")){
                            val photos = actual.getJSONArray("photos")
                            val imageBaseURL = "https://maps.googleapis.com/maps/api/place/photo" +
                                    "?maxwidth=400" +
                                    "&photo_reference=" + photos.getJSONObject(0).getString("photo_reference") +
                                    "&key="+ MAPS_API_KEY
                            //Log.e(TAG, "IMAGE URL: $imageBaseURL")
                            newPlace.image = imageBaseURL
                        } else {
                            newPlace.image = "Aap_uEA7vb0DDYVJWEaX3O-AtYp77AaswQKSGtDaimt3gt7QCNpdjp1BkdM6acJ96xTec3tsV_ZJNL_JP-lqsVxydG3nh739RE_hepOOL05tfJh2_ranjMadb3VoBYFvF0ma6S24qZ6QJUuV6sSRrhCskSBP5C1myCzsebztMfGvm7ij3gZT"
                        }

                        newPlace.id = actual.getString("place_id")
                        newPlace.name = actual.getString("name")

                        newPlace.location.name = actual.getString("name")
                        newPlace.location.latitude = placeLocation.getDouble("lat")
                        newPlace.location.longitude = placeLocation.getDouble("lng")

                        Log.e(TAG, newPlace.toString())
                        result.add(newPlace)

                    }
                }else {
                    Log.e(TAG, "There aren't places with this criteria")
                }

                placesAdapter.clearAdapterData()
                placesAdapter.setupAdapterData(result)

            }, Response.ErrorListener { _ ->
            }) {}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }

    override fun onPlaceSelect(place: TravelPlace) {
        if (!route.listOfPlaces.contains(place)) {
            route.listOfPlaces.add(place)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            Log.e(TAG, "selected: ${parent.getItemAtPosition(position).toString()}")
            generateCategory(parent.getItemAtPosition(position).toString())
            searchPlacesAround(route.endPoint, category, category)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e(TAG, "Nothing in category has been selected")
    }

    private fun generateCategory(selected: String) {
        when (selected) {
            "Restaurant" -> this.category = "restaurant"
            "Hotel" -> this.category = "hotel"
            "Parking" -> this.category = "parking"
            "Gas station" -> this.category = "gas_station"
            "Bar" -> this.category = "bar"
            "Hospital" -> this.category = "hospital"
            "Park" -> this.category = "park"
            "Museum" -> this.category = "museum"
            "Spa" -> this.category = "spa"
            "Tourist Attraction" -> this.category = "tourist_attraction"
            else -> this.category = "tourist_attraction"
        }
    }
}