package repository

import network.WeatherApiService
import network.WeatherResponse

class WeatherRepository(
    private val apiService: WeatherApiService
) {

    suspend fun getWeather(): WeatherResponse {
        return apiService.getWeather(
            latitude = -27.4698,
            longitude = 153.0251
        )
    }
}