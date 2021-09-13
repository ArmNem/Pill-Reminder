package com.example.pillreminder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pillreminder.data.BEPill
import kotlinx.coroutines.flow.Flow


@Dao
interface PillDAO {

    fun getPills(query: String,sortOrder: SortOrder): Flow<List<BEPill>> =
      when(sortOrder){
          SortOrder.BY_NAME -> getPillsSortedByName(query)
      }

    @Query("SELECT * FROM pill_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name DESC, name")
    fun getPillsSortedByName(searchQuery: String): Flow<List<BEPill>>

    @Query("SELECT * from pill_table order by pillId")
    fun getAll(): Flow<List<BEPill>>

    @Query("SELECT name from pill_table order by name")
    fun getAllPills(): Flow<List<String>>

    @Query("SELECT * from pill_table where pillId = (:id)")
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