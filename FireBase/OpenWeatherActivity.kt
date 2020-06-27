package weather.Anchangwan1501023.FireBase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_open_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.security.Permission

class OpenWeatherActivity : AppCompatActivity(), LocationListener {
    private val PERMISSION_REQUEST_CODE = 2000
    private val APP_ID = "b8c9def4d48c67f4c8c97f43d066e833"
    private val UNITS = "metric"
    private val LANGUAGE = "KR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_weather)
        getLocationInfo()
        setting.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))

        }
    }


    private fun drawCurrentWeather(currentWeather: TotalWeather) {
        with(currentWeather) {

            this.weatherList?.getOrNull(0)?.let {
                it.icon?.let {
                    val glide = Glide.with(this@OpenWeatherActivity)
                    glide
                        .load(Uri.parse("https://openweathermap.org/img/w/" + it + ".png"))
                        .into(current_icon)
                }
                it.main?.let { current_main.text = it }
                it.description?.let { current_description.text = it }
            }

            this.main?.temp?.let { current_now.text = String.format("%.1f", it) }
            this.main?.tempMax?.let { current_max.text = String.format("%.1f", it) }
            this.main?.tempMin?.let { current_min.text = String.format("%.1f", it) }


        }
    }

    private fun getLocationInfo() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this@OpenWeatherActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@OpenWeatherActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                reqeustWeatherInfoOfLocation(latitude = latitude, longitude = longitude)
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0F,
                    this
                )
                locationManager.removeUpdates(this)
            }
        }
    }

    private fun reqeustWeatherInfoOfLocation(latitude: Double, longitude: Double) {
        (application as WeatherApplication)
            .requestService()
            ?.getWeatherInfoOfCoordinates(
                latitude = latitude,
                logitude = longitude,
                appID = APP_ID,
                units = UNITS,
                language = LANGUAGE

            )
            ?.enqueue(object : Callback<TotalWeather> {
                override fun onFailure(call: Call<TotalWeather>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<TotalWeather>,
                    response: Response<TotalWeather>
                ) {
                    if (response.isSuccessful) {
                        val totalWeather = response.body()
                        totalWeather?.let {
                            drawCurrentWeather(totalWeather)
                        }
                    }
                }
            })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getLocationInfo()
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        val latitude = location?.latitude
        val longitude = location?.longitude
        if (latitude != null && longitude != null) {
            reqeustWeatherInfoOfLocation(latitude = latitude, longitude = longitude)
        }

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }


}





