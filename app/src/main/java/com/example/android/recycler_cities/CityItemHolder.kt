package com.example.android.recycler_cities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.TempConstants
import com.example.android.response.WeatherResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_city.*

class CityItemHolder(
    override val containerView: View,
    private val clickLambda: (WeatherResponse) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(weatherResponse: WeatherResponse) {
        city_name_tv.text = weatherResponse.name
        city_temp_tv.text = weatherResponse.main.temp.toString()
        val temp = weatherResponse.main.temp.toInt()
        var colorTemp = 0
        when {
            temp < TempConstants.minus30 -> colorTemp = R.color.tempVeryCold
            temp < TempConstants.minus10 && temp >= TempConstants.minus30 -> colorTemp =
                R.color.tempCold
            temp <= TempConstants.plus10 && temp >= TempConstants.minus10 -> colorTemp =
                R.color.tempCool
            temp in TempConstants.plus10 + 1 until TempConstants.plus30 -> colorTemp =
                R.color.tempWarm
            temp > TempConstants.plus30 -> colorTemp = R.color.tempHot
        }
        city_temp_tv.setTextColor(getColor(containerView.context, colorTemp))
        itemView.setOnClickListener {
            clickLambda(weatherResponse)
        }

    }

    companion object {

        fun create(parent: ViewGroup, clickLambda: (WeatherResponse) -> Unit) =
            CityItemHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_city,
                    parent,
                    false
                ),
                clickLambda
            )
    }
}
