package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelerVehicle(
    var year: String = "1900",
    var maker: String = "Unknown",
    var model: String = "Unknown"
): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "year" to year,
            "maker" to maker,
            "model" to model,
        )
    }
}
