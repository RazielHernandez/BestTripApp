package com.fekea.besttripapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.dataModel.TravelerVehicle
import com.fekea.besttripapp.databinding.CardviewTravelBinding
import com.fekea.besttripapp.databinding.CardviewVehicleBinding
import com.fekea.besttripapp.interfaces.TravelInterface
import com.fekea.besttripapp.interfaces.VehicleInterface

class VehicleAdapter (private val context: Context, private val listener: VehicleInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val adapterItems = arrayListOf<TravelerVehicle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardviewVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VehicleViewHolder).bind(adapterItems[position])
    }

    override fun getItemCount(): Int {
        return adapterItems.size
    }

    fun clearAdapterData() {
        adapterItems.clear()
    }

    fun removeData(route: TravelerVehicle) {
        adapterItems.remove(route)
        notifyDataSetChanged()
    }

    fun addPlace(route: TravelerVehicle) {
        adapterItems.add(route)
        notifyDataSetChanged()
    }

    fun setupAdapterData(items: List<TravelerVehicle>) {
        adapterItems.addAll(items)
        notifyDataSetChanged()
    }
}