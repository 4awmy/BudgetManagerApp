package com.example.mobile_development_project

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String,
    val date: Long
)

