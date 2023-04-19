package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelPlace (
    var id: String = "",
    var name: String = "",
    var placeId: String = "",
    var location: TravelLocation = TravelLocation(),
    var image: String = "",
    var category: String = ""

): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "placeId" to placeId,
            "location" to location,
            "image" to image,
            "category" to category
        )
    }
}