package com.projectdelta.optimize.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.LayoutRvCvrpBinding

class RecyclerViewCVRPAdapter( mContext : Context ) : RecyclerView.Adapter< RecyclerViewCVRPAdapter.LayoutViewHolder > ( ) {

	lateinit var binding : LayoutRvCvrpBinding
	lateinit var data : List<Worker>

	inner class LayoutViewHolder( binding: LayoutRvCvrpBinding ) : RecyclerView.ViewHolder( binding.root )

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		binding = LayoutRvCvrpBinding.inflate( LayoutInflater.from(parent.context) , parent , false )
		return LayoutViewHolder( binding )
	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.apply {
			binding.rvCvrpTwCount.text = data[position].route.size.toString()
			binding.rvCvrpTwDistance.text = data[position].distanceTravelled.toString()
			binding.rvCvrpTwId.text = data[position].workerId.toString()
		}
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) return data.size
		return 0
	}

	fun set( x : List<Worker> ){
		data = x
		notifyDataSetChanged()
	}
}