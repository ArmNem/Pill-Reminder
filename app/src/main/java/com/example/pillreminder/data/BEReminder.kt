package com.example.pillreminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Time

@Entity(
    tableName = "reminder_table",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = BEPill::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pill")
        )
    )
)
@Parcelize
data class BEReminder(
    var pillId: Int,
    var alarmTime: Long,
    var isEveryDay: Boolean,
    var dose: String,
    var isActive: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable