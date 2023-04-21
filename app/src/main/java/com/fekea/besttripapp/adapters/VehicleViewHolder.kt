package com.fekea.besttripapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.R
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.dataModel.TravelerVehicle
import com.fekea.besttripapp.databinding.CardviewVehicleBinding
import com.fekea.besttripapp.interfaces.TravelInterface
import com.fekea.besttripapp.interfaces.VehicleInterface
import com.squareup.picasso.Picasso

class VehicleViewHolder(
    private val binding: CardviewVehicleBinding,
    private val listener: VehicleInterface,
    private val context: Context
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TravelerVehicle) {
        //Log.e("HOLDER", "Init card ${item.id}")
        binding.cardVehicleMaker.text = item.maker
        binding.cardVehicleYear.text = item.year.toString()
        binding.cardVehicleModel.text = item.model

        binding.cardButtonDelete.setOnClickListener {
            listener.onVehicleDelete(item)
        }
    }
}