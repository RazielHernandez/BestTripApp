package com.fekea.besttripapp.utils

import android.content.Context
import com.fekea.besttripapp.dataModel.FuelConsumptionModel
import java.io.InputStreamReader

class FuelConsumption(context1: Context, id1: Int) {

    val context: Context
    val id: Int

    init {
        context = context1
        id = id1
    }

    /**
     * fuelConsumptionSearch
     * Search into the database for teh gas consumption for a specific vehicle
     * @param year Year of the car
     * @param maker Vehicle maker (Honda, Mazda, etc)
     * @param model Vehicle full model
     * @author Carlos Hernandez
     * @return FuelConsumptionModel with the three gas consumptions or zero if doesn't find a match
     */
    fun fuelConsumptionSearch(year: String, maker: String, model: String): FuelConsumptionModel {
        var result = FuelConsumptionModel()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){
            val tokens = line.split(",")
            if (year.equals(tokens[0]) && maker.equals(tokens[1]) && model.equals(tokens[2])){
                //Log.e("TAG", "BINGO!!! your consumption is: "+tokens[9])
                result.year = tokens[0]
                result.maker = tokens[1]
                result.model = tokens[2]
                result.fuelOnCity = tokens[8].toFloat()
                result.fuelOnHighway = tokens[9].toFloat()
                result.fuelOnCombined = tokens[10].toFloat()
            }
        }
        return result
    }
}