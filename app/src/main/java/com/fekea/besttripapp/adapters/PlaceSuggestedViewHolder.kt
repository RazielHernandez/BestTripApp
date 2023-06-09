package com.fekea.besttripapp.adapters

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.databinding.CardviewPlaceSuggestedBinding
import com.squareup.picasso.Picasso

class PlaceSuggestedViewHolder(
    private val binding: CardviewPlaceSuggestedBinding,
    private val listener: PlaceInterface,
    private val context: Context): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TravelPlace) {
        //Log.e("HOLDER", "Init card ${item.id}")
        binding.cardName.text = item.name
        Picasso.with(context).load(item.image).into(binding.cardImage)

        binding.cardButtonAdd.setOnClickListener {
            listener.onPlaceSelect(item)
        }
    }
}