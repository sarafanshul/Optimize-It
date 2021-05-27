package com.projectdelta.optimize.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.LayoutRvBinPackingBinding
import com.projectdelta.optimize.databinding.LayoutRvLoadBinding
import com.projectdelta.optimize.service.BinPackingResponse
import com.projectdelta.optimize.util.BinPackingDataConverter
import kotlin.random.Random


class RecyclerViewBinPackingAdapter(mContext : Context) : RecyclerView.Adapter< RecyclerViewBinPackingAdapter.LayoutViewHolder > ( ) {
	lateinit var data : List<Worker>
	lateinit var binding : LayoutRvBinPackingBinding
	lateinit var dataResponse : BinPackingDataConverter

	inner class LayoutViewHolder( binding : LayoutRvBinPackingBinding ) : RecyclerView.ViewHolder( binding.root )

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
		binding = LayoutRvBinPackingBinding.inflate( LayoutInflater.from( parent.context ) , parent , false )
		return LayoutViewHolder( binding )
	}

	override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
		holder.apply {
			binding.rvBinPackingTwId.text = data[position].workerId.toString()
			// value shows percentage of total wokers
			// weight shows current worker capacity %
			binding.rvBinPackingPbValue.max = 100
			binding.rvBinPackingPbValue.progress = Random.nextInt(100)

			binding.rvBinPackingPbWeight.max = 100
			binding.rvBinPackingPbWeight.progress = Random.nextInt(100)
		}
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) return data.size
		return 0
	}

	fun set( _data : List<Worker> ){
		data = _data
		notifyDataSetChanged()
	}
}