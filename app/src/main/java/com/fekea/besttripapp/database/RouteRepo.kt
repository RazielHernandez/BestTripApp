package com.fekea.besttripapp.database

import android.content.Context
import androidx.room.Room
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.utils.ModelEntityConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteRepo(appContext: Context)  {

    private val database = Room.databaseBuilder(appContext, RouteDatabase::class.java, "routes").build()

    suspend fun insertRouteToDatabase(route: TravelRoute) {
        withContext(Dispatchers.IO) {
            database.routeDAO().upsertAll(ModelEntityConverter.fromRouteModelToRouteEntity(route))
        }
    }
}