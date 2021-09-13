package com.example.pillreminder.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName = "pill_table"
)
@Parcelize
data class BEPill(
    var name: String,
    var dose: String,
    var type: String,
    var daytime: String,
    var description: String,
    @PrimaryKey(autoGenerate = true) var pillId: Int = 0
) : Parcelable