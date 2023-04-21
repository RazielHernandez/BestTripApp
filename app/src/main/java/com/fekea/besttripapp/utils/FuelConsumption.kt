package com.fekea.besttripapp.utils

import android.content.Context
import com.fekea.besttripapp.dataModel.FuelConsumptionModel
import java.io.InputStreamReader

class FuelConsumption(context1: Context, id1: Int) {

    companion object {
        const val YEAR_OPTION = 0
        const val MAKER_OPTION = 1
        const val MODEL_OPTION = 2
    }

    val context: Context
    val id: Int

    init {
        context = context1
        id = id1
    }

    fun getVehicles(): ArrayList<String> {
        var result: ArrayList<String> = ArrayList()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){

            val tokens = line.split(",")
            if (tokens[MAKER_OPTION] != "Make"){
                result.add("${tokens[MAKER_OPTION]} - ${tokens[YEAR_OPTION]} - ${tokens[MODEL_OPTION]}")
            }
        }
        return result
    }

    fun getMakers(): List<String> {
        var result: MutableList<String> = mutableListOf()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){
            val tokens = line.split(",")
            result.add(tokens[MAKER_OPTION])
        }
        return result
    }

    fun getYearsByMaker(maker: String): List<String> {
        var result: MutableList<String> = mutableListOf()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){
            val tokens = line.split(",")
            if (tokens[MAKER_OPTION] == maker && !result.contains(tokens[YEAR_OPTION])){
                result.add(tokens[YEAR_OPTION])
            }
        }
        return result
    }

    fun getModelsByMakerAndYear(maker: String, year: String): List<String> {
        var result: MutableList<String> = mutableListOf()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){
            val tokens = line.split(",")
            if (tokens[MAKER_OPTION] == maker && tokens[YEAR_OPTION] == year && !result.contains(tokens[MODEL_OPTION])){
                result.add(tokens[MODEL_OPTION])
            }
        }
        return result
    }

    fun fuelConsumptionSearch(data: String): FuelConsumptionModel {

        val dataTokens = data.split(" - ")
        val year = dataTokens[YEAR_OPTION]
        val maker = dataTokens[MAKER_OPTION]
        val model = dataTokens[MODEL_OPTION]

        var result = FuelConsumptionModel()
        val insr = context.resources.openRawResource(id)
        val br = InputStreamReader(insr)
        val lines = br.readLines()
        br.close()
        insr.close()
        for (line in lines){
            val tokens = line.split(",")
            if (year.equals(tokens[YEAR_OPTION]) && maker.equals(tokens[MAKER_OPTION]) && model.equals(tokens[MODEL_OPTION])){
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