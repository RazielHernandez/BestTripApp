package com.fekea.besttripapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RouteDAO {

    @Insert(onConflict = REPLACE)
    suspend fun upsertAll(routes: RouteEntity)

    @Query("SELECT * FROM routes")
    suspend fun getData(): List<RouteEntity>

    @Query("DELETE FROM routes")
    suspend fun deleteData()

    @Delete
    suspend fun deleteFromDatabase(route: RouteEntity)
}