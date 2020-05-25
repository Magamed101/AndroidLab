package com.example.android.recycler_city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detailed_city.*

class CityDetItemHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: DataPair) {
        parameter_name_tv.text = data.name
        parameter_value_tv.text = data.value
    }

    companion object {
        fun create(parent: ViewGroup) = CityDetItemHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_detailed_city,
                parent,
                false
            )
        )
    }
}
