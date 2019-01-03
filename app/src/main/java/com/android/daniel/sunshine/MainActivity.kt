package com.android.daniel.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.daniel.sunshine.data.SunshinePreferences
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mWeatherTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWeatherTextView = findViewById(R.id.tv_weather_data)

        loadWeatherData()
    }

    fun loadWeatherData() {
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(arrayOf(location))
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
