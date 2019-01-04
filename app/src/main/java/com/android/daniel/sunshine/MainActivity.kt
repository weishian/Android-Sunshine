package com.android.daniel.sunshine

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.android.daniel.sunshine.data.SunshinePreferences
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mWeatherTextView: TextView
    private lateinit var mErrorMessageDisplay: TextView
    private lateinit var mLoadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWeatherTextView = findViewById(R.id.tv_weather_data)
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display)
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator)
        loadWeatherData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_refresh) {
            mWeatherTextView.setText("")
            loadWeatherData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadWeatherData() {
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(location)
    }

    private fun showWeatherDataView() {
        mErrorMessageDisplay.visibility = View.INVISIBLE
        mWeatherTextView.visibility = View.VISIBLE
    }

    private fun showErrorMessage() {
        mErrorMessageDisplay.visibility = View.VISIBLE
        mWeatherTextView.visibility = View.INVISIBLE
    }

    inner class FetchWeatherTask : AsyncTask<String, Void, Array<String>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mLoadingIndicator.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): Array<String>? {
            if (params.isEmpty()) {
                return null
            }
            val location = params[0]
            val weatherRequestUrl = NetworkUtils.buildUrl(location)
            return try {
                val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl!!)
                OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse!!)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(weatherData: Array<String>?) {
            mLoadingIndicator.visibility = View.INVISIBLE
            if (weatherData != null) {
                showWeatherDataView()
                for (weatherString in weatherData) {
                    mWeatherTextView.append(weatherString + "\n\n\n")
                }
            }
            else {
                showErrorMessage()
            }
        }

    }
}
