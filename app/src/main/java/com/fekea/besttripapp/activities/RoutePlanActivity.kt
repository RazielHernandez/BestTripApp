package com.fekea.besttripapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityPlanRouteBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.ArrayList

class RoutePlanActivity: AppCompatActivity() {

    companion object {
        const val TAG = "besttripapp.RoutePlanActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityPlanRouteBinding

    private lateinit var origin: LatLng
    private lateinit var destination: LatLng
    private var originName: String = ""
    private var destinationName: String = ""
    private var stopsLatLng: ArrayList<LatLng> = ArrayList()
    private var stopsNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Autocomplete fragment to search destination
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.view?.setBackgroundColor(Color.WHITE)
        autocompleteFragment.setHint("Introduce route point")

        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.i(TAG, p0.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (originName == "") {
                    val textOrigin = findViewById<TextView>(R.id.text_origin)
                    origin = p0.latLng
                    originName = p0.name
                    Log.e(TAG,"Origin: " + originName)
                    textOrigin.text = originName
                }
                else if (destinationName == "") {
                    val textDestination = findViewById<TextView>(R.id.text_destination)
                    destination = p0.latLng
                    destinationName = p0.name
                    Log.e(TAG,"Destination: " + destinationName)
                    textDestination.text = destinationName
                }
                else {
                    val textStops = findViewById<TextView>(R.id.text_stops)
                    stopsLatLng.add(p0.latLng)
                    stopsNames.add(p0.name)
                    Log.e(TAG, p0.name)
                    var stops: String = ""
                    for(i in stopsNames.indices) {
                        stops += stopsNames[i] + "\n"
                    }
                    textStops.text = stops
                }
            }
        })

        val showButton = findViewById<Button>(R.id.show_button)
        showButton.setOnClickListener() {

            /*val newRoute = TravelRoute()
            val legs = routes.getJSONObject(routeSelected).getJSONArray("legs")
            newRoute.startPoint = TravelLocation(name= "Toronto", latitude = origin.latitude, longitude = origin.longitude)
            newRoute.endPoint = TravelLocation(name = destinationName ,latitude = destination.latitude, longitude = destination.longitude)
            newRoute.name = destinationName
            newRoute.route = routes[routeSelected].toString()
            newRoute.distance = legs.getJSONObject(0).getJSONObject("distance").getDouble("value")
            newRoute.duration = legs.getJSONObject(0).getJSONObject("duration").getDouble("value")
            newRoute.distanceString = legs.getJSONObject(0).getJSONObject("distance").getString("text")
            newRoute.durationString = legs.getJSONObject(0).getJSONObject("duration").getString("text")

            val myIntent = Intent(this, ShowRouteActivity::class.java)
            myIntent.putExtra("search_route", newRoute)
            startActivity(myIntent)*/



            val myIntent = Intent(this, ShowRouteActivity::class.java)
            val newRoute = TravelRoute()
            //val legs = rout.getJSONObject(routeSelected).getJSONArray("legs")
            newRoute.startPoint = TravelLocation(name= "Toronto", latitude = origin.latitude, longitude = origin.longitude)
            newRoute.endPoint = TravelLocation(name = destinationName ,latitude = destination.latitude, longitude = destination.longitude)
            newRoute.name = destinationName
            newRoute.waypoints = stopsNames
            //newRoute.route = routes[routeSelected].toString()
            //newRoute.distance = legs.getJSONObject(0).getJSONObject("distance").getDouble("value")
            //duration = legs.getJSONObject(0).getJSONObject("duration").getDouble("value")
            //newRoute.distanceString = legs.getJSONObject(0).getJSONObject("distance").getString("text")
            //newRoute.durationString = legs.getJSONObject(0).getJSONObject("duration").getString("text")
            myIntent.putExtra("show_route", newRoute)
            myIntent.putExtra("waypoints", stopsLatLng)
            startActivity(myIntent)
        }
    }
}