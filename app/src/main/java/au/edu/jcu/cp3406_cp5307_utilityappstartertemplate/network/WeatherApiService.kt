package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,weather_code",
        @Query("timezone") timezone: String = "Australia/Brisbane"
    ): WeatherResponse
}