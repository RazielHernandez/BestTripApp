package com.fekea.besttripapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.databinding.CardviewPlaceSavedBinding
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.squareup.picasso.Picasso

class PlaceSavedViewHolder (
    private val binding: CardviewPlaceSavedBinding,
    private val listener: PlaceInterface,
    private val context: Context): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TravelPlace) {
            binding.cardName.text = item.name
            Picasso.with(context).load(item.image).into(binding.cardImage)

            binding.cardButtonDelete.setOnClickListener {
                listener.onPlaceSelect(item)
            }
        }
}