package com.example.android.recycler_cities

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.response.WeatherResponse

class CityAdapter(
    private var dataSource: List<WeatherResponse>,
    private val clickLambda: (WeatherResponse) -> Unit
) : RecyclerView.Adapter<CityItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemHolder =
        CityItemHolder.create(parent, clickLambda)

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: CityItemHolder, position: Int) =
        holder.bind(dataSource[position])
}
