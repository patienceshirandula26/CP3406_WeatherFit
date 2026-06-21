package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.WeatherFitTheme
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.util.AlertSoundPlayer
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel.WeatherUiState
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel.WeatherViewModel
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // ThemeMode lives here (outside UtilityApp) because it controls
            // the WeatherFitTheme wrapper itself, not just content inside it.
            var themeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
            val useDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            WeatherFitTheme(darkTheme = useDarkTheme) {
                UtilityApp(
                    themeMode = themeMode,
                    onThemeModeChange = { themeMode = it }
                )
            }
        }
    }
}

/** User's appearance preference. SYSTEM follows the device's day/night setting. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    WeatherFitTheme {
        UtilityApp(themeMode = ThemeMode.SYSTEM, onThemeModeChange = {})
    }
}

@Composable
fun UtilityApp(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    // rememberSaveable (not remember) so these survive screen rotation instead
    // of silently resetting to their defaults.
    var selectedTab by rememberSaveable { mutableStateOf("Utility") }
    var useFahrenheit by rememberSaveable { mutableStateOf(false) }
    var soundAlertEnabled by rememberSaveable { mutableStateOf(true) }

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
                    soundAlertEnabled = soundAlertEnabled,
                    weatherUiState = weatherUiState,
                    onRefreshWeather = { weatherViewModel.refreshWeather() }
                )
                "Settings" -> SettingsScreen(
                    useFahrenheit = useFahrenheit,
                    onUnitChanged = { useFahrenheit = it },
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    soundAlertEnabled = soundAlertEnabled,
                    onSoundAlertChanged = { soundAlertEnabled = it }
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(
    useFahrenheit: Boolean,
    soundAlertEnabled: Boolean,
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

    // Play the alert at most once per condition change, not on every recomposition.
    var alertPlayedFor by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(condition, soundAlertEnabled) {
        if (soundAlertEnabled && condition.contains("storm", ignoreCase = true) && alertPlayedFor != condition) {
            AlertSoundPlayer.playSevereWeatherAlert()
            alertPlayedFor = condition
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

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
    onUnitChanged: (Boolean) -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    soundAlertEnabled: Boolean,
    onSoundAlertChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)

        // ---- Temperature unit ----
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = "  Temperature Unit",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = "Choose how temperatures are shown across the app.",
                    style = MaterialTheme.typography.bodyMedium
                )
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
            }
        }

        // ---- Appearance ----
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (themeMode == ThemeMode.DARK) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "  Appearance", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    text = "Override the app's light/dark mode, or follow your device setting.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ThemeMode.entries.forEach { mode ->
                        if (themeMode == mode) {
                            Button(onClick = { onThemeModeChange(mode) }, shape = RoundedCornerShape(16.dp)) {
                                Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                            }
                        } else {
                            OutlinedButton(onClick = { onThemeModeChange(mode) }, shape = RoundedCornerShape(16.dp)) {
                                Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                            }
                        }
                    }
                }
            }
        }

        // ---- Severe weather sound alert ----
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(text = "  Severe Weather Sound Alert", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = "Play a short alert tone when a thunderstorm is forecast.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                Switch(checked = soundAlertEnabled, onCheckedChange = onSoundAlertChanged)
            }
        }

        Text(
            text = "Current unit: ${if (useFahrenheit) "Fahrenheit" else "Celsius"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}