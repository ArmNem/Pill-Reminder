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
import java.sql.Time
import java.time.LocalTime

@Database(entities = [BEPill::class, BEReminder::class], version =1)
abstract class PillDatabase : RoomDatabase() {

    abstract fun pillDao(): PillDAO
    abstract fun reminderDao(): RemiderDAO
    class Callback @Inject constructor(
        private val database: Provider<PillDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val pilldao = database.get().pillDao()
            val reminderdao = database.get().reminderDao()
            val pill = BEPill(
                "Red pill",
                "1 tablet per day",
                "vitamin",
                "Morning",
                "Take this pill once a day with food"
            )
            applicationScope.launch {
                pilldao.insert(
                    BEPill(
                        "Blue pill",
                        "2 tablets per day",
                        "vitamin",
                        "Morning",
                        "Take this pill 2 times a day with food"
                    )
                )
                pilldao.insert(
                    BEPill(
                        "Red pill",
                        "1 tablet per day",
                        "vitamin",
                        "Morning",
                        "Take this pill once a day with food"
                    )
                )
                reminderdao.insert(
                    BEReminder(
                         Long.MAX_VALUE,
                        false, "1", false,false,0, pill
                    )
                )
                reminderdao.insert(
                    BEReminder(
                        Long.MAX_VALUE,
                        false, "2", false,false,0,pill
                    )
                )
            }

        }
    }
}