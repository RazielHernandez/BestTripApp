package com.fekea.besttripapp.dataModel

import java.io.Serializable

data class TravelerUser(
    var _id: String = "null",
    var name: String? = null,
    var lastName: String? = null,
    var login: String? = null,
    var password: String? = null,
    var email: String? = null,
    var vehicles: List<TravelerVehicle>? = emptyList()
): Serializable {
    fun modelToHashMapOf(): HashMap<String, Any?> {
        return hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "login" to login,
            "password" to password,
            "email" to email,
            "vehicles" to vehicles
        )
    }
}
