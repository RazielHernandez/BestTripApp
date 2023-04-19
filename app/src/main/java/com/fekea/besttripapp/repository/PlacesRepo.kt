package com.fekea.besttripapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fekea.besttripapp.activities.RouteActivity
import com.fekea.besttripapp.dataModel.TravelLocation
import com.fekea.besttripapp.dataModel.TravelPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class PlacesRepo (): ViewModel() {

    private val _placesListLiveData = MutableLiveData<List<TravelPlace>>()
    val placesListLiveData: LiveData<List<TravelPlace>> = _placesListLiveData


    companion object {
        const val TAG = "com.fekea.besttripapp.PlacesRepo"
        const val MAPS_API_KEY = "AIzaSyCOK0WAAutVBjR1gDyrkjJwgGUrRvZL_Jk"
    }

    fun searchPlacesAround(context: Context, location: TravelLocation, category: String, keyword: String) {

        viewModelScope.launch(Dispatchers.IO) {
            var result: MutableList<TravelPlace> = mutableListOf()
            var urlQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?keyword=" + keyword +
                    "&location=" + location.latitude.toString() + "," + location.longitude.toString() +
                    "&radius=15000" +
                    "&type=" + category +
                    "&key=" + RouteActivity.MAPS_API_KEY

            Log.e(RouteActivity.TAG, urlQuery)


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

                        Log.e(RouteActivity.TAG, "PLACE: $actual")

                        newPlace.id = actual.getString("place_id")
                        newPlace.name = actual.getString("name")

                        newPlace.location.name = actual.getString("name")
                        newPlace.location.latitude = placeLocation.getDouble("lat")
                        newPlace.location.longitude = placeLocation.getDouble("lng")
                        newPlace.image = photos.getJSONObject(0).getString("photo_reference")

                        Log.e(RouteActivity.TAG, newPlace.toString())
                        result.add(newPlace)
                    }

                }, Response.ErrorListener { _ ->
                }) {}

            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(directionsRequest)

            _placesListLiveData.postValue(result)
        }
    }
}