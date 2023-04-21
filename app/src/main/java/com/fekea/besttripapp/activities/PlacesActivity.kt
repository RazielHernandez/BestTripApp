package com.fekea.besttripapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityPlacesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.adapters.PlaceSuggestedAdapter
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.fekea.besttripapp.viewModel.RouteViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject

class PlacesActivity: AppCompatActivity(), PlaceInterface, OnMapReadyCallback, OnItemSelectedListener {

    companion object {
        const val TAG = "besttripapp.PlacesActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityPlacesBinding
    private lateinit var mMap: GoogleMap
    private lateinit var route: TravelRoute
    private lateinit var category: String
    private lateinit var placesAdapter: PlaceSuggestedAdapter
    private lateinit var placesArray: ArrayList<TravelPlace>
    private lateinit var routeModel: RouteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        route = intent.extras?.getSerializable("route") as TravelRoute

        routeModel = RouteViewModel(this)

        val categoriesSpinner: Spinner = findViewById(R.id.places_category)
        ArrayAdapter.createFromResource(
            this,
            R.array.place_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoriesSpinner.adapter = adapter
        }
        categoriesSpinner.onItemSelectedListener = this

        placesAdapter = PlaceSuggestedAdapter(this, this)
        val recyclerView: RecyclerView = findViewById(R.id.places_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = placesAdapter

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.places_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val titleText: TextView = findViewById(R.id.places_travel_name)
        titleText.text = "Trip to ${route.name}"
    }

    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap

        Log.e(TAG, "On map ready with ${route.endPoint}")
        //Map Settings
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        //listeners
        //mMap.setOnInfoWindowClickListener(this)
        //mMap.setOnPolylineClickListener(this)

        getLocation()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(route.endPoint.latitude, route.endPoint.longitude), 9.0f))
    }

    private fun putMarketsOnMap() {
        mMap.clear()
        for (place in placesArray) {
            Log.e(TAG, "Adding marker for ${place.name}")
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(place.location.latitude, place.location.longitude))
                    .title(place.name)
            )
        }

        for (place in route.listOfPlaces) {
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(place.location.latitude, place.location.longitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
    }

    fun searchPlacesAround(location: TravelLocation, category: String, keyword: String) {
        placesArray = ArrayList()
        var urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                //"?keyword=" + keyword +
                "?location=" + location.latitude.toString() + "," + location.longitude.toString() +
                "&radius=15000" +
                "&type=" + category +
                "&key=" + SaveActivity.MAPS_API_KEY

        Log.e(RouteActivity.TAG, urlQuery)

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
                                    "&key="+ RouteActivity.MAPS_API_KEY
                            //Log.e(TAG, "IMAGE URL: $imageBaseURL")
                            newPlace.image = imageBaseURL

                        } else {
                            newPlace.image = "Aap_uEA7vb0DDYVJWEaX3O-AtYp77AaswQKSGtDaimt3gt7QCNpdjp1BkdM6acJ96xTec3tsV_ZJNL_JP-lqsVxydG3nh739RE_hepOOL05tfJh2_ranjMadb3VoBYFvF0ma6S24qZ6QJUuV6sSRrhCskSBP5C1myCzsebztMfGvm7ij3gZT"
                        }
                        newPlace.category = category
                        newPlace.id = actual.getString("place_id")
                        newPlace.name = actual.getString("name")

                        newPlace.location.name = actual.getString("name")
                        newPlace.location.latitude = placeLocation.getDouble("lat")
                        newPlace.location.longitude = placeLocation.getDouble("lng")

                        Log.e(RouteActivity.TAG, newPlace.toString())
                        placesArray.add(newPlace)

                    }
                }else {
                    Log.e(RouteActivity.TAG, "There aren't places with this criteria")
                }

                placesAdapter.clearAdapterData()
                placesAdapter.setupAdapterData(placesArray)

                putMarketsOnMap()

            }, Response.ErrorListener { _ ->
            }) {}

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            Log.e(RouteActivity.TAG, "selected: ${parent.getItemAtPosition(position).toString()}")
            generateCategory(parent.getItemAtPosition(position).toString())
            searchPlacesAround(route.endPoint, category, category)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e(RouteActivity.TAG, "Nothing in category has been selected")
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

    override fun onPlaceSelect(place: TravelPlace) {
        Log.e(TAG, "Place ${place.name} selected")
        if (!route.listOfPlaces.contains(place)) {
            route.listOfPlaces.add(place)
            placesAdapter.removeData(place)
            routeModel.insertRouteToDB(route)
        }
    }
}