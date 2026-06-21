package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.repository

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.network.WeatherApiService
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.network.WeatherResponse

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