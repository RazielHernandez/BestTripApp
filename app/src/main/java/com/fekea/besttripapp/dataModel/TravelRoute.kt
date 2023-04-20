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
    var listOfCost: MutableList<TravelExtraCost> = mutableListOf(),
    var route: String = "",
    var listOfPlaces: MutableList<TravelPlace> = mutableListOf(),
): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "userId" to userId,
            "startPoint" to startPoint,
            "endPoint" to endPoint,
            "vehicle" to vehicle,
            "gasCost" to gasCost,
            "tollCost" to tollCost,
            "distance" to distance,
            "duration" to duration,
            "listOfCost" to listOfCost,
            "route" to route,
            "listOfPlaces" to listOfPlaces,
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
