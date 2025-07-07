package com.example.contadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contadora.ui.theme.ContadorAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow // Importa Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de la base de datos, repositorio y ViewModelFactory
        // Esto se hace en onCreate, fuera del ámbito de Compose, y se pasa al setContent
        val database = CounterDatabase.getDatabase(applicationContext)
        val repository = CounterRepository(database.counterDao())
        val viewModelFactory = CounterViewModelFactory(repository)

        setContent {
            ContadorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Se usa el factory para obtener el ViewModel con las dependencias
                    CounterScreen(viewModel = viewModel(factory = viewModelFactory))
                }
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    // currentCount se observa como State para que la UI se recomponga con los cambios
    val currentCount by viewModel.currentCount.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Contador: $currentCount", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { viewModel.incrementCounter() }) {
            Text("Incrementar y Guardar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCounterScreen() {
    ContadorAppTheme {
        // Para el preview, necesitamos un ViewModel mockeado ya que no hay un contexto real de Activity/App
        val mockCounterDao = object : CounterDao {
            private val _mockCounter = MutableStateFlow<Counter?>(Counter(value = 100))
            override suspend fun insert(counter: Counter) {
                _mockCounter.value = counter
            }
            override fun getLastCounter(): Flow<Counter?> = _mockCounter
            override fun getTotalCount(): Flow<Int?> = MutableStateFlow(100)
        }
        val mockRepository = CounterRepository(mockCounterDao)
        val mockViewModel = CounterViewModel(mockRepository)

        // Pasa directamente el ViewModel mock al Composable para el preview
        CounterScreen(viewModel = mockViewModel)
    }
}