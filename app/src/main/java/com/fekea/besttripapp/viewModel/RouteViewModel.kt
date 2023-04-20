package com.fekea.besttripapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.database.RouteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteViewModel(private val context: Context): ViewModel() {

    private val routeRepo = RouteRepo(appContext = context)

    companion object {
        const val TAG = "com.fekea.besttripapp.RouteViewModel"
    }

    private val _routesLiveData = MutableLiveData<List<TravelRoute>>()
    val routesLiveData: LiveData<List<TravelRoute>> = _routesLiveData

    fun insertRouteToDB(route: TravelRoute) {
        Log.e(TAG, "Starting insert for ${route.name}")
        viewModelScope.launch(Dispatchers.IO) {
            routeRepo.insertRouteToDatabase(route)
            Log.e(TAG, "Route Inserted with")
        }
    }

    fun getRoutes() {
        Log.e(TAG, "Getting all the routes")
        viewModelScope.launch(Dispatchers.IO) {
            val data = routeRepo.getAllRoutes()
            _routesLiveData.postValue(data)
        }
    }

}