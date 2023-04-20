package com.fekea.besttripapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey (autoGenerate = true) val id: Int = 1,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "startPoint") val startPoint: LocationEntity,
    @ColumnInfo(name = "endPoint") val endPoint: LocationEntity,
    @ColumnInfo(name = "vehicle") val vehicle: VehicleEntity,
    @ColumnInfo(name = "liters") val liters: Double,
    @ColumnInfo(name = "gasCost") val gasCost: Double,
    @ColumnInfo(name = "tollCost") val tollCost: Double,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "duration") val duration: Double,
    @ColumnInfo(name = "distanceString") val distanceString: String,
    @ColumnInfo(name = "durationString") val durationString: String,
    @ColumnInfo(name = "listOfCost") val listOfCost: List<ExtraCostEntity>,
    @ColumnInfo(name = "route") val route: String,
    @ColumnInfo(name = "listOfPlaces") val listOfPlaces: List<PlaceEntity>,
    /*
    @PrimaryKey (autoGenerate = true) var id: Int = 1,
    @ColumnInfo(name = "name") var name: String = "null",
    @ColumnInfo(name = "userId") var userId: String = "null",
    @ColumnInfo(name = "startPoint") var startPoint: LocationEntity = LocationEntity(),
    @ColumnInfo(name = "endPoint") var endPoint: LocationEntity = LocationEntity(),
    @ColumnInfo(name = "vehicle") var vehicle: VehicleEntity = VehicleEntity(),
    @ColumnInfo(name = "liters") var liters: Double = 0.0,
    @ColumnInfo(name = "gasCost") var gasCost: Double = 0.0,
    @ColumnInfo(name = "tollCost") var tollCost: Double = 0.0,
    @ColumnInfo(name = "distance") var distance: Double = 0.0,
    @ColumnInfo(name = "duration") var duration: Double = 0.0,
    @ColumnInfo(name = "distanceString") var distanceString: String = "",
    @ColumnInfo(name = "durationString") var durationString: String = "",
    @ColumnInfo(name = "listOfCost") var listOfCost: MutableList<ExtraCostEntity> = mutableListOf(),
    @ColumnInfo(name = "route") var route: String = "",
    @ColumnInfo(name = "listOfPlaces") var listOfPlaces: MutableList<PlaceEntity> = mutableListOf(),*/
)

data class LocationEntity(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

data class VehicleEntity(
    val year: Int,
    val maker: String,
    val model: String,
)

data class ExtraCostEntity(
    val tag: String,
    val cost: Float,
)

data class PlaceEntity (
    val id: String,
    val name: String,
    val placeId: String,
    val location: LocationEntity,
    val image: String,
    val category: String,
)
