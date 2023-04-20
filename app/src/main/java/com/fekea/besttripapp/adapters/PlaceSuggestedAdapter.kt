package com.fekea.besttripapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.databinding.CardviewPlaceSuggestedBinding
import com.fekea.besttripapp.interfaces.PlaceInterface

class PlaceSuggestedAdapter(private val context: Context, private val listener: PlaceInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterItems = arrayListOf<TravelPlace>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardviewPlaceSuggestedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceSuggestedViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaceSuggestedViewHolder).bind(adapterItems[position])
    }

    override fun getItemCount(): Int {
        return adapterItems.size
    }

    fun clearAdapterData() {
        adapterItems.clear()
    }

    fun addPlace(place: TravelPlace) {
        adapterItems.add(place)
        notifyDataSetChanged()
    }

    fun setupAdapterData(items: List<TravelPlace>) {
        adapterItems.addAll(items)
        Log.e("TAG", "items added ${items.size}")
        notifyDataSetChanged()
    }
}