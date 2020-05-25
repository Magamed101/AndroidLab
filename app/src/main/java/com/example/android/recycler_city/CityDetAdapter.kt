package com.example.android.recycler_city

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CityDetAdapter(private val dataSrc: List<DataPair>) :
    RecyclerView.Adapter<CityDetItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityDetItemHolder =
        CityDetItemHolder.create(parent)

    override fun getItemCount(): Int = dataSrc.size

    override fun onBindViewHolder(holder: CityDetItemHolder, position: Int) =
        holder.bind(dataSrc[position])
}
