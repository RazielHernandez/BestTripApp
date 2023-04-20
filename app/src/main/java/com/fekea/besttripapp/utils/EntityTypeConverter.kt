package com.fekea.besttripapp.utils

import androidx.room.TypeConverter
import com.fekea.besttripapp.database.ExtraCostEntity
import com.fekea.besttripapp.database.LocationEntity
import com.fekea.besttripapp.database.PlaceEntity
import com.fekea.besttripapp.database.VehicleEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object EntityTypeConverter {
    @TypeConverter
    fun fromStringToExtraCost(value: String): ArrayList<ExtraCostEntity> {
        val listType: Type = object : TypeToken<ArrayList<ExtraCostEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromExtraCostList(list: List<ExtraCostEntity>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToPlaceEntity(value: String): ArrayList<PlaceEntity> {
        val listType: Type = object : TypeToken<ArrayList<PlaceEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPlaceEntityList(list: List<PlaceEntity>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToLocation(value: String): LocationEntity {
        val listType: Type = object : TypeToken<LocationEntity>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromLocationList(list: LocationEntity): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToVehicle(value: String): VehicleEntity {
        val listType: Type = object : TypeToken<VehicleEntity>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromVehicleList(list: VehicleEntity): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}