package com.example.pillreminder.Models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PillDAO {

    @Query("SELECT * from BEPill order by id")
    fun getAll(): LiveData<List<BEPill>>

    @Query("SELECT name from BEPill order by name")
    fun getAllPills(): LiveData<List<String>>

    @Query("SELECT * from BEPill where id = (:id)")
    fun getById(id: Int): LiveData<BEPill>

    @Insert
    fun insert(p: BEPill)

    @Update
    fun update(p: BEPill)

    @Delete
    fun delete(p: BEPill)

    @Query("DELETE from BEPill")
    fun deleteAll()
}