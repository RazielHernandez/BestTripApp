package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelExtraCost(
    var tag: String,
    var cost: Float,
): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "tag" to tag,
            "cost" to cost,
        )
    }
}
