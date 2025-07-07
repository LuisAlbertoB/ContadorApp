package com.example.contadora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class CounterViewModel(private val repository: CounterRepository) : ViewModel() {

    private val _currentCount = MutableStateFlow(0)
    val currentCount: StateFlow<Int> = _currentCount.asStateFlow()

    init {
        // Observar el último valor guardado en la base de datos
        viewModelScope.launch {
            repository.lastCounter.collect { counter -> // ¡CAMBIO AQUÍ! De 'getLastCounter()' a 'lastCounter'
                _currentCount.value = counter?.value ?: 0
            }
        }
    }

    fun incrementCounter() {
        viewModelScope.launch {
            val nextValue = _currentCount.value + 1
            // Simula una operación asíncrona de guardado en la base de datos
            repository.insert(Counter(value = nextValue))
            _currentCount.value = nextValue
        }
    }
}

class CounterViewModelFactory(private val repository: CounterRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}