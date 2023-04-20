package com.fekea.besttripapp.utils

import com.fekea.besttripapp.dataModel.*
import com.fekea.besttripapp.database.*

object ModelEntityConverter {

    fun fromRouteModelToRouteEntity(routes: List<TravelRoute>): List<RouteEntity> {
        val list: ArrayList<RouteEntity> = arrayListOf()
        routes.forEach {
            list.add(
                RouteEntity(
                    name = it.name,
                    userId = it.userId,
                    startPoint = fromLocationModelToLocationEntity(it.startPoint),
                    endPoint = fromLocationModelToLocationEntity(it.endPoint),
                    vehicle = fromVehicleModelToVehicleEntity(it.vehicle),
                    liters = it.liters,
                    gasCost = it.gasCost,
                    tollCost = it.tollCost,
                    distance = it.distance,
                    duration = it.duration,
                    image = it.image,
                    category = it.category,
                    date = it.date,
                    distanceString = it.distanceString,
                    durationString = it.durationString,
                    listOfCost = fromExtraCostModelToExtraCostEntity(it.listOfCost),
                    route = it.route,
                    listOfPlaces = fromPlaceModelToPlaceEntity(it.listOfPlaces),
                )
            )
        }
        return list
    }

    fun fromRouteEntityToRouteModel(routes: List<RouteEntity>): List<TravelRoute> {
        val list: ArrayList<TravelRoute> = arrayListOf()
        routes.forEach {
            list.add(
                TravelRoute(
                    name = it.name,
                    userId = it.userId,
                    startPoint = fromLocationEntityToLocationModel(it.startPoint),
                    endPoint = fromLocationEntityToLocationModel(it.endPoint),
                    vehicle = fromVehicleEntityToVehicleModel(it.vehicle),
                    liters = it.liters,
                    gasCost = it.gasCost,
                    tollCost = it.tollCost,
                    distance = it.distance,
                    duration = it.duration,
                    image = it.image,
                    category = it.category,
                    date = it.date,
                    distanceString = it.distanceString,
                    durationString = it.durationString,
                    listOfCost = fromExtraCostEntityToExtraCostModel(it.listOfCost),
                    route = it.route,
                    listOfPlaces = fromPlaceEntityToPlaceModel(it.listOfPlaces),
                )
            )
        }
        return list
    }

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
            image = route.image,
            category = route.category,
            date = route.date,
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
            image = route.image,
            category = route.category,
            date = route.date,
            distanceString = route.distanceString,
            durationString = route.durationString,
            listOfCost =  fromExtraCostEntityToExtraCostModel(route.listOfCost),
            route = route.route,
            listOfPlaces = fromPlaceEntityToPlaceModel(route.listOfPlaces),
        )
    }

    private fun fromLocationModelToLocationEntity(location: TravelLocation): LocationEntity {
        return LocationEntity(
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun fromLocationEntityToLocationModel(location: LocationEntity): TravelLocation {
        return TravelLocation(
            name = location.name,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun fromVehicleModelToVehicleEntity(vehicle: TravelerVehicle): VehicleEntity {
        return VehicleEntity(
            year = vehicle.year,
            maker = vehicle.maker,
            model = vehicle.model
        )
    }

    private fun fromVehicleEntityToVehicleModel(vehicle: VehicleEntity): TravelerVehicle {
        return TravelerVehicle(
            year = vehicle.year,
            maker = vehicle.maker,
            model = vehicle.model
        )
    }

    private fun fromExtraCostModelToExtraCostEntity(costs: List<TravelExtraCost>): List<ExtraCostEntity> {
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

    private fun fromExtraCostEntityToExtraCostModel(costs: List<ExtraCostEntity>): MutableList<TravelExtraCost> {
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

    private fun fromPlaceModelToPlaceEntity(costs: List<TravelPlace>): List<PlaceEntity> {
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

    private fun fromPlaceEntityToPlaceModel(costs: List<PlaceEntity>): MutableList<TravelPlace> {
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