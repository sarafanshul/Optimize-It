package com.projectdelta.optimize.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.projectdelta.optimize.R
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Worker

class RecyclerViewEditWorkerAdapter( private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	lateinit var data: List<Worker>

	inner class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(position: Int) {
			itemView.findViewById<MaterialTextView>(R.id.edit_worker_tw_id).text = data[position].workerId.toString()
			itemView.findViewById<MaterialTextView>(R.id.edit_worker_tw_capacity).text = data[position].capacity.toString()
			itemView.findViewById<MaterialTextView>(R.id.edit_worker_tw_dist).text = data[position].maxDistance.toString()
			itemView.findViewById<MaterialTextView>(R.id.edit_worker_tw_route).text = "No route set"
		}
	}

	inner class PlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val btn = itemView.findViewById<MaterialCardView>(R.id.edit_worker_add_cw_add)

		fun bind(position: Int) {
			// using external adapter for this
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			R.layout.layout_rv_edit_worker -> {
				val view = LayoutInflater.from(context)
					.inflate(R.layout.layout_rv_edit_worker, parent, false)
				WorkerViewHolder(view)
			}
			R.layout.layout_rv_edit_worker_add -> {
				val view = LayoutInflater.from(context)
					.inflate(R.layout.layout_rv_edit_worker_add, parent, false)
				PlaceholderViewHolder(view)
			}
			else -> throw IllegalArgumentException("Unknown view type $viewType")
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (getItemViewType(position)) {
			R.layout.layout_rv_edit_worker -> (holder as WorkerViewHolder).bind(position)
			R.layout.layout_rv_edit_worker_add -> (holder as PlaceholderViewHolder).bind(position)
		}
	}

	override fun getItemCount(): Int {
		if (this::data.isInitialized) return data.size + 1
		return 1
	}

	override fun getItemViewType(position: Int): Int {
		if (this::data.isInitialized && position == data.size || !this::data.isInitialized)
			return R.layout.layout_rv_edit_worker_add
		return R.layout.layout_rv_edit_worker

	}

	fun set(_data: List<Worker>) {
		data = _data
		notifyDataSetChanged()
	}
}