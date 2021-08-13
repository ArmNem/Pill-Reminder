package com.example.pillreminder.Models

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BEPill::class], version= 1)
abstract class PillDatabase : RoomDatabase() {

    abstract fun pillDao(): PillDAO
}