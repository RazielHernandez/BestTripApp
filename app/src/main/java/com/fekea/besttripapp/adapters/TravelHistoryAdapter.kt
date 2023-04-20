package com.fekea.besttripapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fekea.besttripapp.dataModel.TravelPlace
import com.fekea.besttripapp.dataModel.TravelRoute
import com.fekea.besttripapp.databinding.CardviewPlaceSuggestedBinding
import com.fekea.besttripapp.databinding.CardviewTravelBinding
import com.fekea.besttripapp.interfaces.PlaceInterface
import com.fekea.besttripapp.interfaces.TravelInterface

class TravelHistoryAdapter (private val context: Context, private val listener: TravelInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterItems = arrayListOf<TravelRoute>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardviewTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelHistoryViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TravelHistoryViewHolder).bind(adapterItems[position])
    }

    override fun getItemCount(): Int {
        return adapterItems.size
    }

    fun clearAdapterData() {
        adapterItems.clear()
    }

    fun removeData(route: TravelRoute) {
        adapterItems.remove(route)
        notifyDataSetChanged()
    }

    fun addPlace(route: TravelRoute) {
        adapterItems.add(route)
        notifyDataSetChanged()
    }

    fun setupAdapterData(items: List<TravelRoute>) {
        adapterItems.addAll(items)
        notifyDataSetChanged()
    }
}