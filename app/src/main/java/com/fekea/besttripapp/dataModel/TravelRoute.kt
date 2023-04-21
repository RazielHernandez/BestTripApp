package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelRoute(
    var _id: String = "null",
    var name: String = "null",
    var userId: String = "null",
    var startPoint: TravelLocation = TravelLocation(),
    var endPoint: TravelLocation = TravelLocation(),
    var vehicle: TravelerVehicle = TravelerVehicle(),
    var liters: Double = 0.0,
    var gasCost: Double = 0.0,
    var tollCost: Double = 0.0,
    var distance: Double = 0.0,
    var duration: Double = 0.0,
    var distanceString: String = "",
    var durationString: String = "",
    var image: String = "",
    var date: String = "",
    var category: String = "",
    var listOfCost: MutableList<TravelExtraCost> = mutableListOf(),
    var route: String = "",
    var listOfPlaces: MutableList<TravelPlace> = mutableListOf(),
    var waypoints: ArrayList<String> = ArrayList(),
): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "userId" to userId,
            "startPoint" to startPoint,
            "endPoint" to endPoint,
            "vehicle" to vehicle,
            "gasCost" to gasCost,
            "liters" to liters,
            "tollCost" to tollCost,
            "distance" to distance,
            "duration" to duration,
            "image" to image,
            "date" to date,
            "distanceStrincg" to distanceString,
            "durationString" to durationString,
            "category" to category,
            "listOfCost" to listOfCost,
            "route" to route,
            "listOfPlaces" to listOfPlaces,
            "waypoint" to waypoints,
        )
    }

    fun calculateTotalCost(): Double {
        var result: Double = 0.0
        result = gasCost + tollCost
        for(extra in listOfCost){
            result += extra.cost
        }
        return result
    }
}
