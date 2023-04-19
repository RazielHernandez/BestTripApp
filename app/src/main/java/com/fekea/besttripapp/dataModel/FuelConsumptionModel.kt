package com.fekea.besttripapp.dataModel

import java.text.DecimalFormat

data class FuelConsumptionModel(
    var year: String = "",
    var maker: String = "",
    var model: String = "",
    var fuelOnCity: Float = 0F,
    var fuelOnHighway: Float = 0F,
    var fuelOnCombined: Float = 0F,
) {
    fun calculateGasConsumption(distance: Double): Double{
        return (distance / 100) * fuelOnCombined
    }

    fun calculateGasConsumptionString(distance: Double): String{
        val formatter = DecimalFormat("#.##")
        val liters = (distance / 100) * fuelOnCombined
        return "${formatter.format(liters)} liters of gas"
    }
}
