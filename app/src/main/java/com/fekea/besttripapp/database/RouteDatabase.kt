package com.fekea.besttripapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fekea.besttripapp.utils.EntityTypeConverter

@Database(entities = [RouteEntity::class], version = 1)
@TypeConverters(EntityTypeConverter::class)
abstract class RouteDatabase: RoomDatabase() {
    abstract fun routeDAO(): RouteDAO
}