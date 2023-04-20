package com.fekea.besttripapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.CardviewTravelBinding
import com.fekea.besttripapp.interfaces.TravelInterface
import com.squareup.picasso.Picasso

class TravelHistoryViewHolder (
    private val binding: CardviewTravelBinding,
    private val listener: TravelInterface,
    private val context: Context
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TravelRoute) {
        //Log.e("HOLDER", "Init card ${item.id}")
        binding.cardName.text = item.name
        binding.cardCategory.text = item.category
        binding.cardDate.text = item.date

        if (item.image.isEmpty()){
            binding.cardImage.setImageResource(R.drawable.luggage)
        }else {
            Picasso.with(context).load(item.image).into(binding.cardImage)
        }

        binding.cardButtonView.setOnClickListener {
            listener.onTravelPressed(item)
        }
    }
}