package com.fekea.besttripapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
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
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "distanceString") val distanceString: String,
    @ColumnInfo(name = "durationString") val durationString: String,
    @ColumnInfo(name = "listOfCost") val listOfCost: List<ExtraCostEntity>,
    @ColumnInfo(name = "route") val route: String,
    @ColumnInfo(name = "listOfPlaces") val listOfPlaces: List<PlaceEntity>,

)

data class LocationEntity(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

data class VehicleEntity(
    val year: String,
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
