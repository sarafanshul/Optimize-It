package com.projectdelta.optimize.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.projectdelta.optimize.databinding.LayoutRvShowJobsBinding

class RecyclerViewShowJobsAdapter :
	RecyclerView.Adapter< RecyclerViewShowJobsAdapter.JobViewHolder > ( ) {

	lateinit var binding : LayoutRvShowJobsBinding
	lateinit var data : List<String>

	inner class JobViewHolder( binding: LayoutRvShowJobsBinding ) : RecyclerView.ViewHolder( binding.root )

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
		binding = LayoutRvShowJobsBinding.inflate( LayoutInflater.from( parent.context ) , parent , false )
		return JobViewHolder( binding )
	}

	override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
		holder.apply {
			binding.rvShowJobsTwName.text = data[position]
		}
	}

	override fun getItemCount(): Int {
		if( this::data.isInitialized ) data.size
		return 0
	}

	fun set(X : List<String>){
		data = X
		notifyDataSetChanged()
	}
}