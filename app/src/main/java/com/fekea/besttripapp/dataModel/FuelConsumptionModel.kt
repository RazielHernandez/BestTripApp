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
    fun calculateLiterConsumption(distance: Double): Double{
        return (distance / 100) * fuelOnCombined
    }

    fun calculateLiterConsumptionString(distance: Double): String{
        val formatter = DecimalFormat("#.##")
        val liters = (distance / 100) * fuelOnCombined
        return "${formatter.format(liters)} liters of gas"
    }

    fun calculateCostConsumption(distance: Double): Double{
        val liters = calculateLiterConsumption(distance)
        return liters * 164.9 / 100
    }
}
