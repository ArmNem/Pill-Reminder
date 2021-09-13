package com.example.pillreminder.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RemiderDAO {

    @Query("SELECT * from reminder_table order by reminderId")
    fun getAll(): Flow<List<BEReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: BEReminder)

    @Update
    suspend fun update(p: BEReminder)

    @Delete
    suspend fun delete(p: BEReminder)

    @Query("DELETE from reminder_table")
    suspend fun deleteAll()
}