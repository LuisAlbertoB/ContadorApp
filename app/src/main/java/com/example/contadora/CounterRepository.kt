package com.example.contadora

import kotlinx.coroutines.flow.Flow

class CounterRepository(private val counterDao: CounterDao) {
    val lastCounter: Flow<Counter?> = counterDao.getLastCounter()
    val totalCount: Flow<Int?> = counterDao.getTotalCount()

    suspend fun insert(counter: Counter) {
        counterDao.insert(counter)
    }
}