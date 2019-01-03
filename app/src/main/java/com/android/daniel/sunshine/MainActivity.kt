package com.android.daniel.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mWeatherTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWeatherTextView = findViewById(R.id.tv_weather_data)

        val dummyWeatherData = arrayOf(
            "Today, May 17 - Clear - 17°C / 15°C",
            "Tomorrow - Cloudy - 19°C / 15°C",
            "Thursday - Rainy- 30°C / 11°C",
            "Friday - Thunderstorms - 21°C / 9°C",
            "Saturday - Thunderstorms - 16°C / 7°C",
            "Sunday - Rainy - 16°C / 8°C",
            "Monday - Partly Cloudy - 15°C / 10°C",
            "Tue, May 24 - Meatballs - 16°C / 18°C",
            "Wed, May 25 - Cloudy - 19°C / 15°C",
            "Thu, May 26 - Stormy - 30°C / 11°C",
            "Fri, May 27 - Hurricane - 21°C / 9°C",
            "Sat, May 28 - Meteors - 16°C / 7°C",
            "Sun, May 29 - Apocalypse - 16°C / 8°C",
            "Mon, May 30 - Post Apocalypse - 15°C / 10°C")

        for (dummyWeatherDay in dummyWeatherData) {
            mWeatherTextView.append(dummyWeatherDay + "\n\n\n")
        }
    }

    inner class FetchWeatherTask : AsyncTask<Array<String>, Void, Array<String>>() {
        override fun doInBackground(vararg params: Array<String>?): Array<String>? {
            if (params.count() == 0) {
                return null
            }
            val location: String = params[0].toString()
            val weatherRequestUrl = NetworkUtils.buildUrl(location)
            try {
                val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl!!)
                return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse!!)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(weatherData: Array<String>?) {
            if (weatherData != null) {
                for (weatherString in weatherData) {
                    mWeatherTextView.append(weatherString + "\n\n\n")
                }
            }
        }
    }
}
