package com.example.pillreminder.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RemiderDAO {

    @Query("SELECT * from reminder_table order by reminderId")
    fun getAll(): Flow<List<BEReminder>>

    /*@Query("SELECT * from reminder_table WHERE reminderId = reminderId")
    suspend fun getReminderById(id: Int): BEReminder
*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: BEReminder)

   /* @Query("UPDATE reminder_table SET alarmTime = alarmTime WHERE reminderId = reminderId")
    suspend fun updateReminderTime(time: Long, id: Int)
*/
    @Update
    suspend fun update(p: BEReminder)

    @Delete
    suspend fun delete(p: BEReminder)

    @Query("DELETE from reminder_table")
    suspend fun deleteAll()
}