package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate
//imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme.CP3406_CP5603UtilityAppStarterTemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CP3406_CP5603UtilityAppStarterTemplateTheme {
                UtilityApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    CP3406_CP5603UtilityAppStarterTemplateTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    var selectedTab by remember { mutableStateOf("Utility") }
    var useFahrenheit by remember { mutableStateOf(false) }

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
                "Utility" -> UtilityScreen(useFahrenheit = useFahrenheit)
                "Settings" -> SettingsScreen(
                    useFahrenheit = useFahrenheit,
                    onUnitChanged = { useFahrenheit = it}
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(useFahrenheit: Boolean) {

    // Temporary weather values.
    // These will later come from an API.
    val city = "Brisbane"
    val celsiusTemperature = 22
    val fahrenheitTemperature = (celsiusTemperature * 9 / 5) + 32
    val temperature = if (useFahrenheit) {
        "$fahrenheitTemperature°F"
    } else {
        "$celsiusTemperature°C"
    }

    val condition = "Cloudy"
    val sweaterAdvice = "Light sweater recommended"
    val umbrellaAdvice = "Umbrella not needed"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "WeatherFit",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = city,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = temperature,
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = "Condition: $condition",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "🧥 $sweaterAdvice",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "☂️ $umbrellaAdvice",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = {
                // API refresh functionality will be added later.
            }
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
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Temperature Unit",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = { onUnitChanged(false) }
        ) {
            Text("Use Celsius °C")
        }

        Button(
            onClick = { onUnitChanged(true) }
        ) {
            Text("Use Fahrenheit °F")
        }

        Text(
            text = "Current unit: ${if (useFahrenheit) "Fahrenheit" else "Celsius"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}