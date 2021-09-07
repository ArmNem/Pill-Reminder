package com.example.pillreminder.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RemiderDAO {

    @Query("SELECT * from reminder_table order by id")
    fun getAll(): Flow<List<BEReminder>>


    @Query("SELECT * from reminder_table where id = (:id)")
    fun getById(id: Int): Flow<BEReminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: BEReminder)

    @Update
    suspend fun update(p: BEReminder)

    @Delete
    suspend fun delete(p: BEReminder)

    @Query("DELETE from reminder_table")
    suspend fun deleteAll()
}