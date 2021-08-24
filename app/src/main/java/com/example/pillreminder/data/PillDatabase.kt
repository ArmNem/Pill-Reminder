package com.example.pillreminder.data

import androidx.room.Database
import javax.inject.Inject

import javax.inject.Provider
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pillreminder.data.BEPill
import com.example.pillreminder.data.PillDAO
import com.example.pillreminder.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [BEPill::class], version= 1)
abstract class PillDatabase : RoomDatabase() {

    abstract fun pillDao(): PillDAO
    class Callback @Inject constructor(
        private val database: Provider<PillDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().pillDao()
            applicationScope.launch {
                dao.insert(BEPill("Blue pill","2 tablets per day","vitamin","Take this pill 2 times a day with food"))
                dao.insert(BEPill("Red pill","1 tablet per day","vitamin","Take this pill once a day with food"))
            }

        }
    }
}