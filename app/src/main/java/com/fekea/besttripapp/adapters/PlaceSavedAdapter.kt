package com.fekea.besttripapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.databinding.CardviewPlaceSavedBinding
import com.fekea.besttripapp.databinding.CardviewPlaceSuggestedBinding
import com.fekea.besttripapp.interfaces.PlaceInterface

class PlaceSavedAdapter(private val context: Context, private val listener: PlaceInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterItems = arrayListOf<TravelPlace>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardviewPlaceSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceSavedViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaceSavedViewHolder).bind(adapterItems[position])
    }

    override fun getItemCount(): Int {
        return adapterItems.size
    }

    fun clearAdapterData() {
        adapterItems.clear()
    }

    fun removeData(place: TravelPlace) {
        adapterItems.remove(place)
        notifyDataSetChanged()
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