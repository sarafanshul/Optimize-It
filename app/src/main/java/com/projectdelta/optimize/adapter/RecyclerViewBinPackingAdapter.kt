package com.projectdelta.optimize.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.LayoutRvBinPackingBinding
import com.projectdelta.optimize.databinding.LayoutRvLoadBinding
import com.projectdelta.optimize.service.BinPackingResponse
import com.projectdelta.optimize.util.BinPackingDataConverter
import kotlin.random.Random


class RecyclerViewBinPackingAdapter(mContext : Context) : RecyclerView.Adapter< RecyclerViewBinPackingAdapter.LayoutViewHolder > ( ) {
	lateinit var data : List<Worker>
	lateinit var binding : LayoutRvBinPackingBinding
	lateinit var X : List<Container>
	var MAX : Int = 0

	inner class LayoutViewHolder( binding : LayoutRvBinPackingBinding ) : RecyclerView.ViewHolder( binding.root )

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		binding = LayoutRvBinPackingBinding.inflate( LayoutInflater.from( parent.context ) , parent , false )
		return LayoutViewHolder( binding )
	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		var sm = 0L ; var wt = 0L

		data[position].jobs.forEach { sm += X[it].value ; wt += X[it].weight }

		holder.apply {
			binding.rvBinPackingTwId.text = data[position].workerId.toString()
			// value shows percentage of total wokers
			// weight shows current worker capacity %
			binding.rvBinPackingPbValue.max = MAX
			binding.rvBinPackingPbValue.progress = sm.toInt()

			binding.rvBinPackingPbWeight.max = data[position].capacity.toInt()
			binding.rvBinPackingPbWeight.progress = wt.toInt()
		}

		Log.d("RATIO" , "${data[position].capacity.toInt()} : ${wt.toInt()}" )
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) return data.size
		return 0
	}

	fun set( _data : List<Worker> , x : List<Container> , mx : Int ){
		X = x ; MAX = mx ; data = _data
		notifyDataSetChanged()
	}
}