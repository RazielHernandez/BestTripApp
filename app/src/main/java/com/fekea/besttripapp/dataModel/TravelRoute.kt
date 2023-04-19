package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelRoute(
    var _id: String = "null",
    var name: String = "null",
    var userId: String = "null",
    var startPoint: TravelLocation = TravelLocation(),
    var endPoint: TravelLocation = TravelLocation(),
    var vehicle: TravelerVehicle = TravelerVehicle(),
    var gasCost: Float = 0.0F,
    var tollCost: Float = 0.0F,
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
            "listOfCost" to listOfCost,
            "route" to route,
            "listOfPlaces" to listOfPlaces,
        )
    }

    fun calculateTotalCost(): Float {
        var result: Float = 0.0F
        result = gasCost + tollCost
        for(extra in listOfCost){
            result += extra.cost
        }
        return result
    }
}
