package com.projectdelta.optimize.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.databinding.LayoutRvLoadBinding

class RecyclerViewLoadProjectAdapter : RecyclerView.Adapter< RecyclerViewLoadProjectAdapter.LoadViewHolder >( ) {

	lateinit var data : List<Project>
	lateinit var binding : LayoutRvLoadBinding

	inner class LoadViewHolder( binding: LayoutRvLoadBinding ) : RecyclerView.ViewHolder( binding.root )

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadViewHolder {
		binding = LayoutRvLoadBinding.inflate( LayoutInflater.from( parent.context ) )
		return LoadViewHolder( binding )
	}

	override fun onBindViewHolder(holder: LoadViewHolder, position: Int) {
		holder.itemView.apply {
			binding.rvLoadTwName.text = data[position].projectName
		}
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) return data.size
		return 0
	}

	fun set( projects : List<Project> ){
		data = projects
		notifyDataSetChanged()
	}
}