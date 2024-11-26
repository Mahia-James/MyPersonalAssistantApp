package com.example.mypersonalassistant

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

class WeatherUpdatesActivity : AppCompatActivity() {

    companion object {
        const val API_KEY = "5c0e88574ba42c0436641de0473e0537" // Using the API key from your email
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_updates)

        val etLocation: EditText = findViewById(R.id.etLocation)
        val btnGetWeather: Button = findViewById(R.id.btnGetWeather)
        val tvWeatherInfo: TextView = findViewById(R.id.tvWeatherInfo)

        btnGetWeather.setOnClickListener {
            val location = etLocation.text.toString().trim()
            if (location.isNotEmpty()) {
                fetchWeather(location, tvWeatherInfo)
            } else {
                Toast.makeText(this, R.string.enter_location_prompt, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather(location: String, tvWeatherInfo: TextView) {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getWeather(location, API_KEY)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val weatherResponse = response.body()!!
                        val weatherText = getString(
                            R.string.weather_info,
                            weatherResponse.name,
                            weatherResponse.main.temp,
                            weatherResponse.weather[0].description.replaceFirstChar { it.uppercase(Locale.getDefault()) }
                        )
                        tvWeatherInfo.text = weatherText
                    } else {
                        tvWeatherInfo.text = getString(R.string.weather_error)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WeatherUpdatesActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Retrofit API interface
    interface WeatherApi {
        @GET("data/2.5/weather")
        suspend fun getWeather(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric" // Return temperature in Celsius
        ): retrofit2.Response<WeatherResponse>
    }

    // Data classes for response parsing
    data class WeatherResponse(
        val name: String,
        val main: Main,
        val weather: List<Weather>
    )

    data class Main(val temp: Double)

    data class Weather(val description: String)
}
