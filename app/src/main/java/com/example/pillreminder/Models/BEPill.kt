package com.example.pillreminder.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BEPill(
    @PrimaryKey(autoGenerate = true) var id:Int,
    var name: String,
    var dose: String,
    var description: String
)
    : Serializable