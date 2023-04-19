package com.fekea.besttripapp.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.activities.PlaceInterface
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.databinding.CardviewPlaceSuggestedBinding

class placeSuggestedViewHolder(private val binding: CardviewPlaceSuggestedBinding, private val listener: PlaceInterface): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TravelPlace) {
        Log.e("HOLDER", "Init card ${item.id}")
        binding.cardName.text = item.name
        binding.cardCategory.text = item.category

        binding.cardButtonAdd.setOnClickListener {
            listener.onPlaceSelect(item)
        }
    }
}