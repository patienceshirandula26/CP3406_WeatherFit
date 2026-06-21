package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.network.NetworkModule
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.repository.WeatherRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class ForecastItem(
    val day: String,
    val temperature: Int,
    val icon: String
)

data class WeatherUiState(
    val city: String = "Brisbane",
    val temperature: Double = 22.0,
    val apparentTemperature: Double = 21.0,
    val humidity: Int = 65,
    val condition: String = "Cloudy",
    val sweaterAdvice: String = "Light sweater recommended",
    val umbrellaAdvice: String = "Umbrella not needed",
    val forecast: List<ForecastItem> = listOf(
        ForecastItem("Tomorrow", 24, "☀️"),
        ForecastItem("Tuesday", 20, "🌧️"),
        ForecastItem("Wednesday", 23, "⛅")
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository(NetworkModule.weatherApiService)
) : ViewModel() {

    var uiState by mutableStateOf(WeatherUiState(isLoading = true))
        private set

    init {
        refreshWeather()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                val response = repository.getWeather()
                val current = response.current

                uiState = WeatherUiState(
                    city = "Brisbane",
                    temperature = current.temperature,
                    apparentTemperature = current.apparentTemperature,
                    humidity = current.humidity,
                    condition = weatherCodeToCondition(current.weatherCode),
                    sweaterAdvice = sweaterAdvice(current.temperature),
                    umbrellaAdvice = umbrellaAdvice(current.weatherCode),
                    forecast = response.daily.maxTemperatures.drop(1).take(3).mapIndexed { index, temp ->
                        ForecastItem(
                            day = listOf("Tomorrow", "Next Day", "Third Day")[index],
                            temperature = temp.roundToInt(),
                            icon = forecastIcon(response.daily.weatherCodes.getOrElse(index + 1) { 0 })
                        )
                    },
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Could not load weather data"
                )
            }
        }
    }

    private fun sweaterAdvice(temp: Double): String {
        return if (temp < 24) "Light sweater or cardigan recommended"
        else "Light outfit recommended"
    }

    private fun umbrellaAdvice(code: Int): String {
        return if (code in 51..99) "Carry an umbrella"
        else "Umbrella not needed"
    }

    private fun weatherCodeToCondition(code: Int): String {
        return when (code) {
            0 -> "Clear"
            1, 2 -> "Partly cloudy"
            3 -> "Cloudy"
            in 45..48 -> "Foggy"
            in 51..67 -> "Rainy"
            in 80..99 -> "Showers"
            else -> "Unknown"
        }
    }

    private fun forecastIcon(code: Int): String {
        return when (code) {
            0 -> "☀️"
            1, 2 -> "🌤️"
            3 -> "☁️"
            in 51..99 -> "🌧️"
            else -> "⛅"
        }
    }
}