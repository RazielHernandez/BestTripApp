package com.fekea.besttripapp.utils

import com.fekea.besttripapp.dataModel.*
import com.fekea.besttripapp.database.*

object ModelEntityConverter {

    fun fromRouteModelToRouteEntity(route: TravelRoute): RouteEntity {
        return RouteEntity(
            name = route.name,
            userId = route.userId,
            startPoint = fromLocationModelToLocationEntity(route.startPoint),
            endPoint = fromLocationModelToLocationEntity(route.endPoint),
            vehicle = fromVehicleModelToVehicleEntity(route.vehicle),
            liters = route.liters,
            gasCost = route.gasCost,
            tollCost = route.tollCost,
            distance = route.distance,
            duration = route.duration,
            distanceString = route.distanceString,
            durationString = route.durationString,
            listOfCost =  fromExtraCostModelToExtraCostEntity(route.listOfCost),
            route = route.route,
            listOfPlaces = fromPlaceModelToPlaceEntity(route.listOfPlaces),
        )
    }

    fun fromRouteEntityToRouteModel(route: RouteEntity): TravelRoute {
        return TravelRoute(
            _id = route.id.toString(),
            name = route.name,
            userId = route.userId,
            startPoint = fromLocationEntityToLocationModel(route.startPoint),
            endPoint = fromLocationEntityToLocationModel(route.endPoint),
            vehicle = fromVehicleEntityToVehicleModel(route.vehicle),
            liters = route.liters,
            gasCost = route.gasCost,
            tollCost = route.tollCost,
            distance = route.distance,
            duration = route.duration,
            distanceString = route.distanceString,
            durationString = route.durationString,
            listOfCost =  fromExtraCostEntityToExtraCostModel(route.listOfCost),
            route = route.route,
            listOfPlaces = fromPlaceEntityToPlaceModel(route.listOfPlaces),
        )
    }

    fun fromLocationModelToLocationEntity(location: TravelLocation): LocationEntity {
        return LocationEntity(
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    fun fromLocationEntityToLocationModel(location: LocationEntity): TravelLocation {
        return TravelLocation(
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    fun fromVehicleModelToVehicleEntity(vehicle: TravelerVehicle): VehicleEntity {
        return VehicleEntity(
            year = vehicle.year,
            maker = vehicle.maker,
            model = vehicle.model
        )
    }

    fun fromVehicleEntityToVehicleModel(vehicle: VehicleEntity): TravelerVehicle {
        return TravelerVehicle(
            year = vehicle.year,
            maker = vehicle.maker,
            model = vehicle.model
        )
    }

    fun fromExtraCostModelToExtraCostEntity(costs: List<TravelExtraCost>): List<ExtraCostEntity> {
        val list: ArrayList<ExtraCostEntity> = arrayListOf()
        costs.forEach {
            list.add(
                ExtraCostEntity(
                    tag = it.tag,
                    cost = it.cost
                )
            )
        }
        return list
    }

    fun fromExtraCostEntityToExtraCostModel(costs: List<ExtraCostEntity>): MutableList<TravelExtraCost> {
        val list: ArrayList<TravelExtraCost> = arrayListOf()
        costs.forEach {
            list.add(
                TravelExtraCost(
                    tag = it.tag,
                    cost = it.cost
                )
            )
        }
        return list
    }

    fun fromPlaceModelToPlaceEntity(costs: List<TravelPlace>): List<PlaceEntity> {
        val list: ArrayList<PlaceEntity> = arrayListOf()
        costs.forEach {
            list.add(
                PlaceEntity(
                    id = it.id,
                    name = it.name,
                    placeId = it.placeId,
                    location = fromLocationModelToLocationEntity(it.location),
                    image = it.image,
                    category = it.category,
                )
            )
        }
        return list
    }

    fun fromPlaceEntityToPlaceModel(costs: List<PlaceEntity>): MutableList<TravelPlace> {
        val list: ArrayList<TravelPlace> = arrayListOf()
        costs.forEach {
            list.add(
                TravelPlace(
                    id = it.id,
                    name = it.name,
                    placeId = it.placeId,
                    location = fromLocationEntityToLocationModel(it.location),
                    image = it.image,
                    category = it.category,
                )
            )
        }
        return list
    }
}