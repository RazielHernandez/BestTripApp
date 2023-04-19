package com.fekea.besttripapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.ActivityRouteBinding
import com.fekea.besttripapp.repository.PlacesRepo
import org.json.JSONObject

class RouteActivity: AppCompatActivity() {

    companion object {
        const val TAG = "besttripapp.RouteActivity"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    private lateinit var binding: ActivityRouteBinding
    private lateinit var placesRepo: PlacesRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val route = intent.extras?.getSerializable("search_route") as TravelRoute
        Log.e(TAG, "Route from ${route.startPoint.name} to ${route.endPoint.name}")
        Log.e(TAG, "Route: ${route.route}")

        placesRepo = PlacesRepo()
        var placesSuggested =  placesRepo.searchPlacesAround(
            context = this,
            location = route.endPoint,
            category = "Tourist",
            keyword = "Hotel")

        for (place in placesSuggested) {
            Log.e(TAG, "Place ${place.name}")
            Log.e(TAG, "Location ${place.location}")
            Log.e(TAG, "Image id: ${place.image}")
        }
    }


}