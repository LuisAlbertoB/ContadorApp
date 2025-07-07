package com.example.contadora

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter_table")
data class Counter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Int
)