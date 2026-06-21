package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate
//imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.WeatherFitTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.WeatherUiState
import viewmodel.WeatherViewModel
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherFitTheme {
                UtilityApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    WeatherFitTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    var selectedTab by remember { mutableStateOf("Utility") }
    var useFahrenheit by remember { mutableStateOf(false) }

    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherUiState = weatherViewModel.uiState

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Utility") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen(
                    useFahrenheit = useFahrenheit,
                    weatherUiState = weatherUiState,
                    onRefreshWeather = { weatherViewModel.refreshWeather() }
                )
                "Settings" -> SettingsScreen(
                    useFahrenheit = useFahrenheit,
                    onUnitChanged = { useFahrenheit = it }
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(
    useFahrenheit: Boolean,
    weatherUiState: WeatherUiState,
    onRefreshWeather: () -> Unit
) {

    val city = weatherUiState.city

    val celsiusTemperature = weatherUiState.temperature.roundToInt()
    val fahrenheitTemperature = (celsiusTemperature * 9 / 5) + 32
    val temperature = if (useFahrenheit) "$fahrenheitTemperature°F" else "$celsiusTemperature°C"

    val condition = weatherUiState.condition

    val feelsLikeCelsius = weatherUiState.apparentTemperature.roundToInt()
    val feelsLikeFahrenheit = (feelsLikeCelsius * 9 / 5) + 32
    val feelsLike = if (useFahrenheit) "$feelsLikeFahrenheit°F" else "$feelsLikeCelsius°C"

    val humidity = "${weatherUiState.humidity}%"
    val sweaterAdvice = weatherUiState.sweaterAdvice
    val umbrellaAdvice = weatherUiState.umbrellaAdvice

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // Gradient hero banner instead of plain text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "WeatherFit 🌦️",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    text = "At-a-glance outfit advice for today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = city, style = MaterialTheme.typography.titleLarge)
                Text(text = temperature, style = MaterialTheme.typography.displayMedium)
                Text(text = condition, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Feels like $feelsLike", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Humidity: $humidity", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Fit Check", style = MaterialTheme.typography.titleLarge)
                Text(text = "🧥 $sweaterAdvice", style = MaterialTheme.typography.bodyLarge)
                Text(text = "👖 Bottoms: Jeans, trousers or long skirt", style = MaterialTheme.typography.bodyLarge)
                Text(text = "👟 Shoes: Closed shoes recommended", style = MaterialTheme.typography.bodyLarge)
                Text(text = "☂️ $umbrellaAdvice", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "3-Day Forecast", style = MaterialTheme.typography.titleMedium)
                weatherUiState.forecast.forEach { item ->
                    Text(
                        text = "${item.icon} ${item.day}: ${item.temperature}°C",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = onRefreshWeather
        ) {
            Text("Refresh Weather")
        }
    }
}

@Composable
fun SettingsScreen(
    useFahrenheit: Boolean,
    onUnitChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Temperature Unit", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (!useFahrenheit) {
                Button(onClick = { onUnitChanged(false) }, shape = RoundedCornerShape(16.dp)) {
                    Text("Celsius °C")
                }
            } else {
                OutlinedButton(onClick = { onUnitChanged(false) }, shape = RoundedCornerShape(16.dp)) {
                    Text("Celsius °C")
                }
            }
            if (useFahrenheit) {
                Button(onClick = { onUnitChanged(true) }, shape = RoundedCornerShape(16.dp)) {
                    Text("Fahrenheit °F")
                }
            } else {
                OutlinedButton(onClick = { onUnitChanged(true) }, shape = RoundedCornerShape(16.dp)) {
                    Text("Fahrenheit °F")
                }
            }
        }

        Text(
            text = "Current unit: ${if (useFahrenheit) "Fahrenheit" else "Celsius"}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}