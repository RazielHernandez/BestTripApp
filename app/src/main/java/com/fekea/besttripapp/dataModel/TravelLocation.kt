package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelLocation (
    var name: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0

) : Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude,
        )
    }
}
