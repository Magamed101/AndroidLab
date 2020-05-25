package com.example.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.TempConstants.minus10
import com.example.android.TempConstants.minus30
import com.example.android.TempConstants.plus10
import com.example.android.TempConstants.plus30
import com.example.android.recycler_city.CityDetAdapter
import com.example.android.recycler_city.DataPair
import com.example.android.response.WeatherResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class CityActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @Inject
    @Suppress("LateinitUsage")
    lateinit var service: WeatherService

    @Inject
    @Suppress("LateinitUsage")
    lateinit var picasso: Picasso

    companion object {
        const val CITY_ID = "cityId"

        fun createIntent(activity: Activity, cityId: Int) =
            Intent(activity, CityActivity::class.java).apply {
                putExtra(
                    CITY_ID, cityId
                )
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        MyApp().plusSscComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        launch {
            val response = withContext(Dispatchers.IO) {
                service.weatherById(intent.extras?.getInt(CITY_ID) ?: 0)
            }

            city_detailed_name_tv.text = response.name
            status_detailed_tv.text = response.weathers[0].main
            temp_detailed_tv.text = response.main.temp.toString() + " ℃"
            feels_like_detailed_tv.text = response.main.feelsLike.toString() + " ℃"

            val picUrl = "https://openweathermap.org/img/wn/${response.weathers[0].icon}@2x.png"
            picasso.load(picUrl).into(weather_pic)
            val temp = response.main.temp.toInt()
            var colorTemp = 0
            when {
                temp < minus30 -> colorTemp = R.color.tempVeryCold
                temp in minus30 until minus10 -> colorTemp = R.color.tempCold
                temp in minus10..plus10 -> colorTemp = R.color.tempCool
                temp in plus10 + 1 until plus30 -> colorTemp = R.color.tempWarm
                temp > plus30 -> colorTemp = R.color.tempHot
            }

            temp_detailed_tv.setTextColor(getColor(colorTemp))
            setRecInfo(response)
        }
    }

    private fun setRecInfo(response: WeatherResponse) {
        val list: LinkedList<DataPair> = LinkedList()
        list.add(DataPair("Pressure", response.main.pressure.toString() + " hpa"))
        list.add(DataPair("Wind speed", response.wind.speed.toString() + " m/s"))
        var windOrientation = ""
        val deg = response.wind.deg
        when {
            deg < WindConstants.thirty || deg >= WindConstants.threeHundred30 -> windOrientation =
                "North"
            deg < WindConstants.sixty -> windOrientation = "Northeast"
            deg < WindConstants.hundred20 -> windOrientation = "East"
            deg < WindConstants.hundred50 -> windOrientation = "Southeast"
            deg < WindConstants.twoHundred10 -> windOrientation = "South"
            deg < WindConstants.twoHundred40 -> windOrientation = "Southwest"
            deg < WindConstants.threeHundred -> windOrientation = "West"
            deg < WindConstants.threeHundred30 -> windOrientation = "Northwest"
        }
        list.add(DataPair("Wind orientation", windOrientation))
        list.add(DataPair("Humidity", response.main.humidity.toString() + "%"))
        list.add(DataPair("Clouds", response.weathers[0].description))
        list.add(DataPair("Max temperature", response.main.tempMax.toString() + " ℃"))
        list.add(DataPair("Min temperature", response.main.tempMin.toString() + " ℃"))

        city_rec.adapter = CityDetAdapter(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApp().clearSSCComponent()
    }
}
