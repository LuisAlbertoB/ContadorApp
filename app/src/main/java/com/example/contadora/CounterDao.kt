package com.example.contadora

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {
    @Insert
    suspend fun insert(counter: Counter)

    @Query("SELECT * FROM counter_table ORDER BY id DESC LIMIT 1")
    fun getLastCounter(): Flow<Counter?>

    @Query("SELECT SUM(value) FROM counter_table")
    fun getTotalCount(): Flow<Int?>
}