package com.example.pillreminder.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.sql.Time

@Entity(
    tableName = "reminder_table"
)
@Parcelize
data class BEReminder(
    var alarmTime: String,
    var isEveryDay: Boolean,
    var pilldose: String,
    var isActive: Boolean,
    var isTaken: Boolean,
    @PrimaryKey(autoGenerate = true) var reminderId: Int = 0,
    @Embedded
    var pillinreminder: BEPill
) : Parcelable