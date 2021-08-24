package com.example.pillreminder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pillreminder.data.BEPill
import kotlinx.coroutines.flow.Flow


@Dao
interface PillDAO {

    @Query("SELECT * from pill_table order by id")
    fun getAll(): Flow<List<BEPill>>

    @Query("SELECT name from pill_table order by name")
    fun getAllPills(): Flow<List<String>>

    @Query("SELECT * from pill_table where id = (:id)")
    fun getById(id: Int): Flow<BEPill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: BEPill)

    @Update
    suspend fun update(p: BEPill)

    @Delete
    suspend fun delete(p: BEPill)

    @Query("DELETE from pill_table")
    suspend fun deleteAll()
}