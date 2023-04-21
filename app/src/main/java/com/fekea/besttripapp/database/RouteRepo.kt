package com.fekea.besttripapp.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.utils.ModelEntityConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteRepo(appContext: Context)  {

    private val database = Room.databaseBuilder(appContext, RouteDatabase::class.java, "routes").build()

    suspend fun insertRouteToDatabase(route: TravelRoute) {
        withContext(Dispatchers.IO) {
            //route._id = null
            val entity = ModelEntityConverter.fromRouteModelToRouteEntity(route)
            Log.e("TAG", "upsert with id: ${route._id}")
            database.routeDAO().upsertAll(entity)
        }
    }

    suspend fun updateRouteToDatabase(route: TravelRoute) {
        withContext(Dispatchers.IO) {
            database.routeDAO().upsertAll(ModelEntityConverter.fromRouteModelToRouteEntity(route))
        }
    }

    suspend fun deleteRouteToDatabase(route: TravelRoute) {
        withContext(Dispatchers.IO) {
            database.routeDAO().deleteFromDatabase(ModelEntityConverter.fromRouteModelToRouteEntity(route))
        }
    }

    suspend fun getAllRoutes(): List<TravelRoute> {
        return withContext(Dispatchers.IO) {
            val data = ModelEntityConverter.fromRouteEntityToRouteModel(database.routeDAO().getData())
            data
        }
    }
}